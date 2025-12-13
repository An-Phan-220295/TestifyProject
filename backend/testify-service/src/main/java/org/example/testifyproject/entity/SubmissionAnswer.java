package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "submission_answers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubmissionAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private TestSubmission submission;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    private Integer duration;

    private String status;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @OneToMany(mappedBy = "submissionAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubmissionAnswerDetail> details = new ArrayList<>();
}
