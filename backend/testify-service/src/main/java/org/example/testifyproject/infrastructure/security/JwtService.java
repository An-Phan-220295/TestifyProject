package org.example.testifyproject.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.auth.security.AppUserDetails;
import org.example.testifyproject.common.constant.TokenType;
import org.example.testifyproject.infrastructure.redis.RedisService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
    private final RedisService redisService;
    private final JwtProperties props;
    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String TOKEN_VERSION_REDIS = "tokenVersion:";

    @Getter
    private SecretKey key;

    @PostConstruct
    void init() {
        if (props.getSecret() == null || props.getSecret().isBlank()) {
            log.error("JWT secret is missing or blank");
            throw new IllegalStateException(
                    "app.jwt.secret is null/blank. Check configuration."
            );
        }
        this.key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(props.getSecret())
        );
        log.info("JWT service initialized successfully");
    }

    public String generateAccessToken(AppUserDetails user) {
        Instant now = Instant.now();

        log.debug("Generating access token. email={}", user.getEmail());

        int ver = redisService.get(TOKEN_VERSION_REDIS + user.getEmail(), Integer.class);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("ver", ver)
                .claim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(
                        now.plus(props.getAccessTokenMinutes(), ChronoUnit.MINUTES)
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAndSaveRefreshToken(AppUserDetails user) {
        Instant now = Instant.now();

        String refreshToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(
                        now.plus(props.getRefreshTokenDays(), ChronoUnit.DAYS)
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        if (!saveTokenIntoRedis(TokenType.REFRESH_TOKEN, user.getEmail(), refreshToken)) {
            log.warn("Failed to persist refresh token. email={}", user.getEmail());
        }

        return refreshToken;
    }


    public String validateAndExtractUsername(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date exp = parseClaims(token).getExpiration();
        return exp.before(new Date());
    }

    public boolean isTokenValidForUser(String token, AppUserDetails user) {
        Claims c = parseClaims(token);
        String username = c.getSubject();
        // Check token valid
        return username.equals(user.getEmail()) && !isTokenExpired(token) && isValidVersion(token);
    }

    public boolean logout(String authHeader) {
        String token = authHeader.substring(7);
        String email = validateAndExtractUsername(token);

        log.info("Logout request. email={}", email);

        try {
            redisService.delete(REFRESH_PREFIX + email);

            redisService.incrementWithTTL(TOKEN_VERSION_REDIS + email
                    , Duration.ofDays(props.getRefreshTokenDays() + 1)
            );

            return true;
        } catch (Exception e) {
            log.error("Logout failed. email={}", email, e);
            return false;
        }
    }


    private boolean saveTokenIntoRedis(TokenType tokenType, String email, String token) {
        try {
            Duration ttl;
            String key;

            if (tokenType == TokenType.REFRESH_TOKEN) {
                ttl = Duration.ofDays(props.getRefreshTokenDays());
                key = REFRESH_PREFIX + email;
            } else {
                ttl = Duration.ofMinutes(props.getAccessTokenMinutes());
                key = BLACKLIST_PREFIX + email;
            }

            redisService.set(key, token, ttl);

            log.debug("Token persisted. type={}, email={}, ttlSeconds={}",
                    tokenType, email, ttl.getSeconds());

            return true;
        } catch (Exception e) {
            log.error("Failed to persist token. type={}, email={}",
                    tokenType, email, e);
            return false;
        }
    }

    private boolean isValidVersion(String token) {
        Claims claims = parseClaims(token);

        int clientTokenVer = (int) claims.get("ver");
        String email = claims.getSubject();

        String redisVer = redisService.get(TOKEN_VERSION_REDIS + email, String.class);
        if (redisVer == null) {
            return false;
        }

        int currentVersion = Integer.parseInt(redisVer);
        return clientTokenVer == currentVersion;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            String email = validateAndExtractUsername(refreshToken);
            String key = REFRESH_PREFIX + email;
            String storedToken = redisService.get(key, String.class);

            if (storedToken == null) {
                log.warn("Refresh token not found. email={}", email);
                return false;
            }

            if (!storedToken.equals(refreshToken)) {
                log.warn("Refresh token mismatch. email={}", email);
                return false;
            }

            if (isTokenExpired(refreshToken)) {
                log.warn("Refresh token expired. email={}", email);
                return false;
            }

            log.debug("Refresh token valid. email={}", email);
            return true;

        } catch (Exception e) {
            log.warn("Refresh token validation failed. reason={}",
                    e.getClass().getSimpleName());
            return false;
        }
    }
}
