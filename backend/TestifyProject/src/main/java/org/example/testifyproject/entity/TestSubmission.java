package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "test_submissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestSubmission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    private Integer duration;

    private String status;

    @Column(name = "is_passed")
    private Boolean isPassed = Boolean.FALSE;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubmissionAnswer> answers = new ArrayList<>();
}
