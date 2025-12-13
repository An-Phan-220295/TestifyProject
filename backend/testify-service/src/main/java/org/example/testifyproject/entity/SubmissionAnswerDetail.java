package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "submission_answer_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubmissionAnswerDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "submission_answer_id", nullable = false)
    private SubmissionAnswer submissionAnswer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "is_selected")
    private Boolean isSelected = Boolean.FALSE;
}
