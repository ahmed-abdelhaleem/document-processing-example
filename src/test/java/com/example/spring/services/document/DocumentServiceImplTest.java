package com.example.spring.services.document;

import com.example.spring.dtos.document.DocumentType;
import com.example.spring.entities.document.DocumentEntity;
import com.example.spring.entities.user.UserEntity;
import com.example.spring.models.document.DocumentModel;
import com.example.spring.repositories.document.DocumentRepository;
import com.example.spring.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

public class DocumentServiceImplTest {

    private static final String userSid = "e827e23c-960c-4c0a-aeb9-e5a25349ef9a";

    private static final UUID documentSid = UUID.randomUUID();

    private static final String description = "new document";

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        documentService = new DocumentServiceImpl(documentRepository, userRepository);
    }

    @Test
    void create_validDocumentEntity_expectValidDocumentModel() {

        // assign
        doReturn(Optional.of(buildValidUserEntity())).when(userRepository).findBySid(anyString());

        Mockito.doReturn(buildValidDocumentEntity()).when(documentRepository).save(any());

        DocumentModel documentModel = buildValidDocumentModel();

        //act
        Optional<DocumentModel> optionalDocumentModel = documentService.create(documentModel);

        //assert
        assertTrue(optionalDocumentModel.isPresent());
        assertNotNull(optionalDocumentModel.get().getCreationTime());
        assertNotNull(optionalDocumentModel.get().getUpdateTime());
        assertNotNull(optionalDocumentModel.get().getSid());
        assertEquals(documentModel.getDescription(), optionalDocumentModel.get().getDescription());
        assertEquals(documentModel.getDocumentType(), optionalDocumentModel.get().getDocumentType());
    }

    @Test
    void create_userNotFound_expectEmptyDocumentModel() {

        // assign
        doReturn(Optional.empty()).when(userRepository).findBySid(anyString());

        DocumentModel documentModel = buildValidDocumentModel();

        //act
        Optional<DocumentModel> optionalDocumentModel = documentService.create(documentModel);

        //assert
        assertFalse(optionalDocumentModel.isPresent());
    }

    @Test
    void findOne_validDocumentEntity_expectValidDocumentModel() {

        // assign
        DocumentEntity documentEntity = buildValidDocumentEntity();

        doReturn(Optional.of(documentEntity)).when(documentRepository).findOneBySidAndUserSid(anyString(), anyString());

        //act
        Optional<DocumentModel> optionalDocumentModel = documentService.findOne(documentSid, UUID.fromString(userSid));

        //assert
        assertTrue(optionalDocumentModel.isPresent());
        assertNotNull(optionalDocumentModel.get().getCreationTime());
        assertNotNull(optionalDocumentModel.get().getUpdateTime());
        assertEquals(documentEntity.getSid(), optionalDocumentModel.get().getSid().toString());
        assertEquals(documentEntity.getDescription(), optionalDocumentModel.get().getDescription());
        assertEquals(documentEntity.getType(), optionalDocumentModel.get().getDocumentType().getType());
    }

    @Test
    void findOne_documentNotFound_expectEmptyDocumentModel() {

        // assign
        doReturn(Optional.empty()).when(documentRepository).findOneBySidAndUserSid(anyString(), anyString());

        // act
        Optional<DocumentModel> optionalDocumentModel = documentService.findOne(documentSid, UUID.fromString(userSid));

        // assert
        assertFalse(optionalDocumentModel.isPresent());
    }

    @Test
    void updateDescription_validDocumentEntity_expectValidDocumentModel() {

        // assign
        doReturn(Optional.of(buildValidDocumentEntity())).when(documentRepository).findOneBySid(anyString());

        DocumentEntity documentEntity = buildValidDocumentEntity();
        doReturn(documentEntity).when(documentRepository).save(any());

        // act
        Optional<DocumentModel> optionalDocumentModel = documentService.updateDescription(documentSid, description);

        //assert
        assertTrue(optionalDocumentModel.isPresent());
        assertNotNull(optionalDocumentModel.get().getCreationTime());
        assertNotNull(optionalDocumentModel.get().getUpdateTime());
        assertEquals(documentEntity.getSid(), optionalDocumentModel.get().getSid().toString());
        assertEquals(documentEntity.getDescription(), optionalDocumentModel.get().getDescription());
        assertEquals(documentEntity.getType(), optionalDocumentModel.get().getDocumentType().getType());
    }

    @Test
    void updateDescription_documentNotFound_expectEmptyDocumentModel() {

        // assign
        doReturn(Optional.empty()).when(userRepository).findBySid(anyString());

        // act
        Optional<DocumentModel> optionalDocumentModel = documentService.updateDescription(documentSid, description);

        // assert
        assertFalse(optionalDocumentModel.isPresent());
    }

    private DocumentEntity buildValidDocumentEntity() {

        return DocumentEntity.builder()
                .sid(documentSid.toString())
                .type(DocumentType.JPG.getType())
                .description(description)
                .user(buildValidUserEntity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private DocumentModel buildValidDocumentModel() {

        return DocumentModel.builder()
                .sid(documentSid)
                .documentType(DocumentType.JPG)
                .description(description)
                .userSid(UUID.fromString(userSid))
                .build();
    }

    private UserEntity buildValidUserEntity() {

        return UserEntity.builder()
                .sid(userSid)
                .id(Long.MAX_VALUE)
                .build();
    }
}
