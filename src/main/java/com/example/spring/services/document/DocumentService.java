package com.example.spring.services.document;

import com.example.spring.models.document.DocumentModel;

import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    Optional<DocumentModel> create(DocumentModel documentModel);

    Optional<DocumentModel> findOne(UUID documentSid, UUID userSid);

    Optional<DocumentModel> updateDescription(UUID documentSid, String description);
}
