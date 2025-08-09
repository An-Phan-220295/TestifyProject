package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Test extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "total_score", precision = 5, scale = 2)
    private BigDecimal totalScore;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "is_public")
    private Boolean isPublic = Boolean.TRUE;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();
}
