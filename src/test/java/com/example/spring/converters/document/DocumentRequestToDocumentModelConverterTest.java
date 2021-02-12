package com.example.spring.converters.document;

import com.example.spring.dtos.document.DocumentRequest;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.models.document.DocumentModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentRequestToDocumentModelConverterTest {

    private final DocumentRequestToDocumentModelConverter documentRequestToDocumentModelConverter
            = new DocumentRequestToDocumentModelConverter();

    @Test
    void convert_validDocumentRequest_expectValidDocumentModel() {

        //assign
        DocumentRequest documentRequest = buildValidDocumentRequest();

        //act
        DocumentModel documentModel = documentRequestToDocumentModelConverter.convert(documentRequest);

        //assert
        assertEquals(documentRequest.getDescription(), documentModel.getDescription());
        assertEquals(documentRequest.getDocumentType(), documentModel.getDocumentType().getType());
        assertEquals(documentRequest.getUserId(), documentModel.getUserSid());
    }

    private DocumentRequest buildValidDocumentRequest() {

        return DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .description("new document")
                .userId(UUID.randomUUID())
                .build();
    }
}
