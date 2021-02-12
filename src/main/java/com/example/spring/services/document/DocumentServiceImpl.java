package com.example.spring.services.document;

import com.example.spring.converters.document.DocumentEntityToDocumentModelConverter;
import com.example.spring.converters.document.DocumentModelToDocumentEntityConverter;
import com.example.spring.models.document.DocumentModel;
import com.example.spring.repositories.document.DocumentRepository;
import com.example.spring.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentModelToDocumentEntityConverter documentModelToDocumentEntityConverter
            = new DocumentModelToDocumentEntityConverter();

    private final DocumentEntityToDocumentModelConverter documentEntityToDocumentModelConverter
            = new DocumentEntityToDocumentModelConverter();

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    @Autowired
    public DocumentServiceImpl(final DocumentRepository documentRepository, final UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<DocumentModel> create(final DocumentModel documentModel) {

        return userRepository.findBySid(documentModel.getUserSid().toString())
                .flatMap(userEntity -> {
                    documentModel.setSid(UUID.randomUUID());
                    documentModel.setCreationTime(LocalDateTime.now());
                    documentModel.setUpdateTime(LocalDateTime.now());
                    return documentModelToDocumentEntityConverter.convert(documentModel, userEntity)
                            .map(documentRepository::save);
                })
                .flatMap(documentEntityToDocumentModelConverter::convert);

    }

    @Override
    public Optional<DocumentModel> findOne(UUID documentSid, UUID userSid) {

        return documentRepository.findOneBySidAndUserSid(documentSid.toString(), userSid.toString())
                .flatMap(documentEntityToDocumentModelConverter::convert);
    }

    @Override
    public Optional<DocumentModel> updateDescription(UUID documentSid, String description) {

        return documentRepository.findOneBySid(documentSid.toString())
                .map(documentEntity -> {
                    documentEntity.setDescription(description);
                    documentEntity.setUpdatedAt(LocalDateTime.now());
                    return documentRepository.save(documentEntity);
                }).flatMap(documentEntityToDocumentModelConverter::convert);
    }
}
