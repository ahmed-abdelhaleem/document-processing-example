package com.example.spring.converters.document;

import com.example.spring.dtos.document.DocumentType;
import com.example.spring.entities.document.DocumentEntity;
import com.example.spring.entities.user.UserEntity;
import com.example.spring.models.document.DocumentModel;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentModelToDocumentEntityConverterTest {

    private static final String userSid = "e827e23c-960c-4c0a-aeb9-e5a25349ef9a";

    private final DocumentModelToDocumentEntityConverter documentModelToDocumentEntityConverter
            = new DocumentModelToDocumentEntityConverter();

    @Test
    void convert_validDocumentModel_expectValidDocumentEntity() {

        //assign
        DocumentModel documentModel = buildValidDocumentModel();

        //act
        Optional<DocumentEntity> optionalDocumentEntity =
                documentModelToDocumentEntityConverter.convert(documentModel, buildValidUserEntity());

        //assert
        assertTrue(optionalDocumentEntity.isPresent());
        assertEquals(documentModel.getSid().toString(), optionalDocumentEntity.get().getSid());
        assertEquals(documentModel.getDocumentType().getType(), optionalDocumentEntity.get().getType());
        assertEquals(documentModel.getDescription(), optionalDocumentEntity.get().getDescription());
        assertEquals(documentModel.getUserSid().toString(), optionalDocumentEntity.get().getUser().getSid());
    }

    private UserEntity buildValidUserEntity() {

        return UserEntity.builder()
                .sid(userSid)
                .id(Long.MAX_VALUE)
                .build();
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
