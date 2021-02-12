package com.example.spring.dtos.document;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class DocumentResponse {

    private UUID sid;

    private String documentType;

    private String description;

    private LocalDateTime creationTime;

    private LocalDateTime updateTime;
}
