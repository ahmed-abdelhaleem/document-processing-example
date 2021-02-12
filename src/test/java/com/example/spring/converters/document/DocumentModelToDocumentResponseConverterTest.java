package com.example.spring.converters.document;

import com.example.spring.dtos.document.DocumentResponse;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.models.document.DocumentModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentModelToDocumentResponseConverterTest {

    private static final String userSid = "e827e23c-960c-4c0a-aeb9-e5a25349ef9a";

    private final DocumentModelToDocumentResponseConverter documentModelToDocumentResponseConverter
            = new DocumentModelToDocumentResponseConverter();

    @Test
    void convert_validDocumentModel_expectValidDocumentResponse() {

        //assign
        DocumentModel documentModel = buildValidDocumentModel();

        //act
        DocumentResponse documentResponse = documentModelToDocumentResponseConverter.convert(documentModel);

        //assert
        assertEquals(documentModel.getSid(), documentResponse.getSid());
        assertEquals(documentModel.getDescription(), documentResponse.getDescription());
        assertEquals(documentModel.getDocumentType().getType(), documentResponse.getDocumentType());
        assertEquals(documentModel.getCreationTime(), documentResponse.getCreationTime());
        assertEquals(documentModel.getUpdateTime(), documentResponse.getUpdateTime());
    }

    private DocumentModel buildValidDocumentModel() {

        return DocumentModel.builder()
                .sid(UUID.randomUUID())
                .documentType(DocumentType.JPG)
                .description("new Doc")
                .userSid(UUID.fromString(userSid))
                .build();
    }
}
