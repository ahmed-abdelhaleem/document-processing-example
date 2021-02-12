package com.example.spring.models.document;

import com.example.spring.dtos.document.DocumentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class DocumentModel {

    private UUID sid;

    private DocumentType documentType;

    private String description;

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;

    private UUID userSid;
}
