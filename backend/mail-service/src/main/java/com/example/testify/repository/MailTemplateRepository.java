package com.example.testify.repository;

import com.example.testify.entity.MailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailTemplateRepository extends JpaRepository<MailTemplate, Integer> {
    Optional<MailTemplate> findMailTemplateByTemplateName(String templateName);
}
