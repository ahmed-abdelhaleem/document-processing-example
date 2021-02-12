package com.example.spring.dtos.document;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum DocumentType {

    PDF("pdf"), JPG("jpg"), PNG("png");

    @Getter
    private final String type;

    DocumentType(String type) {
        this.type = type;
    }

    public static Optional<DocumentType> getDocumentType(final String type) {

        return Optional.ofNullable(type).flatMap(doctype -> Arrays.stream(DocumentType.values())
                .filter(documentType -> documentType.getType().equalsIgnoreCase(doctype))
                .findFirst());
    }

}
