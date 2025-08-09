package org.example.testifyproject.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "answer_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnswerImage extends BaseEntity {
    @Id
    @Column(name = "answer_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Column(nullable = false)
    private String url;

    @Column(name = "alt_text")
    private String altText;

    private String caption;
}
