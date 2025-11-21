package com.example.testify.entity;

import com.example.testify.common.type.MailType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailTemplate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mail_type")
    @Enumerated(EnumType.STRING)
    private MailType mailType;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "subject")
    private String subject;

    @Column(name = "template", columnDefinition = "text")
    private String template;
}
