package com.example.spring.repositories.document;

import com.example.spring.entities.document.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    Optional<DocumentEntity> findOneBySid(String sid);

    Optional<DocumentEntity> findOneBySidAndUserSid(String sid, String userSid);
}
