package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "question_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String url;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "\"order\"")
    private Integer order;

    private String caption;
}
