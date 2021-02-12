package com.example.spring.converters.document;

import com.example.spring.converters.BaseConverter;
import com.example.spring.dtos.document.DocumentResponse;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.models.document.DocumentModel;

import java.util.Optional;

public class DocumentModelToDocumentResponseConverter implements BaseConverter<DocumentModel, DocumentResponse> {

    @Override
    public DocumentResponse convert(DocumentModel original) {

        return Optional.ofNullable(original)
                .map(documentModel -> DocumentResponse.builder()
                        .sid(documentModel.getSid())
                        .documentType(Optional.ofNullable(documentModel.getDocumentType()).map(DocumentType::getType).orElse(null))
                        .description(documentModel.getDescription())
                        .creationTime(documentModel.getCreationTime())
                        .updateTime(documentModel.getUpdateTime())
                        .build()).orElse(null);
    }
}
