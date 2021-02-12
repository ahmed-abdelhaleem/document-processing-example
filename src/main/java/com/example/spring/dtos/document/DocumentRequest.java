package com.example.spring.dtos.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.UUID;

@Builder
@Getter
public class DocumentRequest {

    @NotNull
    @JsonProperty("userId")
    private UUID userId;

    @NotNull
    @JsonProperty("type")
    private String documentType;

    @Size(min = 5, message = "description cannot be less than 5 chars")
    @JsonProperty("description")
    private String description;
}
