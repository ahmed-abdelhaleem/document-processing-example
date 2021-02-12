package com.example.spring.converters.document;

import com.example.spring.dtos.document.DocumentType;
import com.example.spring.entities.document.DocumentEntity;
import com.example.spring.entities.user.UserEntity;
import com.example.spring.models.document.DocumentModel;

import java.util.Optional;
import java.util.UUID;

public class DocumentModelToDocumentEntityConverter {

    public Optional<DocumentEntity> convert(DocumentModel original, UserEntity userEntity) {

        return Optional.ofNullable(original)
                .map(documentModel -> DocumentEntity.builder()
                        .sid(Optional.ofNullable(original.getSid()).map(UUID::toString).orElse(UUID.randomUUID().toString()))
                        .createdAt(documentModel.getCreationTime())
                        .updatedAt(documentModel.getUpdateTime())
                        .description(documentModel.getDescription())
                        .user(userEntity)
                        .type(Optional.ofNullable(documentModel.getDocumentType()).map(DocumentType::getType).orElse(null))
                        .build());
    }
}
