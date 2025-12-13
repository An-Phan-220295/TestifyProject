package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "sections")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "is_randomized")
    private Boolean isRandomized = Boolean.FALSE;

    @Column(name = "number_questions")
    private Integer numberQuestions;

    @Column(name = "total_score", precision = 5, scale = 2)
    private BigDecimal totalScore;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}
