package com.example.spring.converters.document;

import com.example.spring.converters.BaseConverter;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.entities.document.DocumentEntity;
import com.example.spring.models.document.DocumentModel;

import java.util.Optional;
import java.util.UUID;

public class DocumentEntityToDocumentModelConverter implements BaseConverter<DocumentEntity, Optional<DocumentModel>> {

    @Override
    public Optional<DocumentModel> convert(DocumentEntity original) {

        return Optional.ofNullable(original)
                .map(documentEntity -> DocumentModel.builder()
                        .sid(Optional.ofNullable(documentEntity.getSid()).map(UUID::fromString).orElse(null))
                        .documentType(DocumentType.getDocumentType(documentEntity.getType()).orElse(null))
                        .description(documentEntity.getDescription())
                        .creationTime(documentEntity.getCreatedAt())
                        .updateTime(documentEntity.getUpdatedAt())
                        .userSid(UUID.fromString(original.getUser().getSid()))
                        .build());
    }

}
