package com.example.spring.controllers.document;

import com.example.spring.converters.document.DocumentModelToDocumentResponseConverter;
import com.example.spring.converters.document.DocumentRequestToDocumentModelConverter;
import com.example.spring.dtos.document.DocumentRequest;
import com.example.spring.dtos.document.DocumentResponse;
import com.example.spring.dtos.document.DocumentType;
import com.example.spring.errorhandling.exceptions.BusinessException;
import com.example.spring.models.document.DocumentModel;
import com.example.spring.services.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRequestToDocumentModelConverter documentRequestToDocumentModelConverter
            = new DocumentRequestToDocumentModelConverter();

    private final DocumentModelToDocumentResponseConverter documentModelToDocumentResponseConverter
            = new DocumentModelToDocumentResponseConverter();

    @Value("${com.example.document.patch.acceptedFields}")
    private List<String> acceptedPatchFields;

    @Value("${com.example.document.patch.description.min.length}")
    private int minimumDescriptionLength;

    private final DocumentService documentService;

    @Autowired
    public DocumentController(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentResponse> createDocumentRecord(@RequestBody @Valid DocumentRequest documentRequest) throws BusinessException {

        validateDocumentType(documentRequest.getDocumentType());

        DocumentModel documentModel = documentRequestToDocumentModelConverter.convert(documentRequest);

        Optional<DocumentModel> optionalDocumentModel
                = documentService.create(documentModel);

        return optionalDocumentModel.map(documentModelToDocumentResponseConverter::convert)
                .map(documentResponse -> new ResponseEntity<>(documentResponse, HttpStatus.CREATED))
                .orElseThrow(() ->
                        new BusinessException("User not found", Optional.of(HttpStatus.UNPROCESSABLE_ENTITY)));
    }

    @GetMapping(value = "/{document_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable("document_id") UUID documentId, @RequestParam("userId") UUID userId) throws BusinessException {

        return documentService.findOne(documentId, userId)
                .map(documentModelToDocumentResponseConverter::convert)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new BusinessException("Document not found", Optional.of(HttpStatus.NOT_FOUND)));
    }

    @PatchMapping(value = "/{document_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentResponse> updateDocumentDescription(@PathVariable("document_id") UUID documentId,
                                                                      @RequestBody Map<String, Object> updateMap) throws BusinessException {

        CollectionUtils.filter(updateMap.values(), Objects::nonNull);

        boolean validPatchFields = acceptedPatchFields.containsAll(updateMap.keySet());

        if (!validPatchFields) {
            throw new BusinessException("Only " + acceptedPatchFields + " can be updated", Optional.of(HttpStatus.UNPROCESSABLE_ENTITY));
        }

        return Optional.ofNullable((String) updateMap.get("description"))
                .filter(description -> description.length() >= minimumDescriptionLength)
                .flatMap(description -> documentService.updateDescription(documentId, description))
                .map(documentModelToDocumentResponseConverter::convert)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new BusinessException("Only description can be updated with minimum length " + minimumDescriptionLength, Optional.of(HttpStatus.UNPROCESSABLE_ENTITY)));
    }

    private void validateDocumentType(String documentType) throws BusinessException {

        DocumentType.getDocumentType(documentType)
                .orElseThrow(() -> new BusinessException("Unsupported document type", Optional.of(HttpStatus.UNPROCESSABLE_ENTITY)));
    }
}
