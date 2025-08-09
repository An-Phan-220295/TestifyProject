package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.testifyproject.entity.enums.Gender;
import org.example.testifyproject.entity.enums.Status;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    private Instant dob;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.OTHER;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "email_verified")
    private Boolean emailVerified = Boolean.FALSE;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    private Set<Test> testsCreated = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Question> questionsCreated = new HashSet<>();
}
