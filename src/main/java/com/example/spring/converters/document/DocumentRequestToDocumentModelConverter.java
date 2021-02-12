package com.example.spring.converters.document;

import com.example.spring.converters.BaseConverter;
import com.example.spring.dtos.document.DocumentRequest;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.models.document.DocumentModel;

import java.util.Optional;

public class DocumentRequestToDocumentModelConverter implements BaseConverter<DocumentRequest, DocumentModel> {

    @Override
    public DocumentModel convert(DocumentRequest original) {

        return Optional.ofNullable(original)
                .map(documentRequest -> DocumentModel.builder()
                        .userSid(documentRequest.getUserId())
                        .description(documentRequest.getDescription())
                        .documentType(DocumentType.getDocumentType(documentRequest.getDocumentType()).orElse(null))
                        .build())
                .orElse(null);
    }
}
