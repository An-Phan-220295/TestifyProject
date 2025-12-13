package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "answers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_correct")
    private Boolean isCorrect = Boolean.FALSE;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @OneToOne(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private AnswerImage image;
}
