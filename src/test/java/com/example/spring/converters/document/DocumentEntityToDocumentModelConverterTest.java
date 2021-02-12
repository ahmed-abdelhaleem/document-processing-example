package com.example.spring.converters.document;

import com.example.spring.dtos.document.DocumentType;
import com.example.spring.entities.document.DocumentEntity;
import com.example.spring.entities.user.UserEntity;
import com.example.spring.models.document.DocumentModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentEntityToDocumentModelConverterTest {

    private static final String userSid = "e827e23c-960c-4c0a-aeb9-e5a25349ef9a";

    private static final UUID documentSid = UUID.randomUUID();

    private final DocumentEntityToDocumentModelConverter documentEntityToDocumentModelConverter
            = new DocumentEntityToDocumentModelConverter();

    @Test
    void convert_validDocumentEntity_expectValidDocumentModel() {

        //assign
        DocumentEntity documentEntity = buildValidDocumentEntity();

        //act
        Optional<DocumentModel> optionalDocumentModel =
                documentEntityToDocumentModelConverter.convert(documentEntity);

        //assert
        assertTrue(optionalDocumentModel.isPresent());
        assertEquals(documentEntity.getSid(), optionalDocumentModel.get().getSid().toString());
        assertEquals(documentEntity.getType(), optionalDocumentModel.get().getDocumentType().getType());
        assertEquals(documentEntity.getDescription(), optionalDocumentModel.get().getDescription());
        assertEquals(documentEntity.getUser().getSid(), optionalDocumentModel.get().getUserSid().toString());
    }


    private DocumentEntity buildValidDocumentEntity() {

        return DocumentEntity.builder()
                .sid(documentSid.toString())
                .type(DocumentType.JPG.getType())
                .description("new Document")
                .user(buildValidUserEntity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private UserEntity buildValidUserEntity() {

        return UserEntity.builder()
                .sid(userSid)
                .id(Long.MAX_VALUE)
                .build();
    }
}
