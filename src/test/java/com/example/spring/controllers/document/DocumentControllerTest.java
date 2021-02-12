package com.example.spring.controllers.document;

import com.example.spring.dtos.document.DocumentRequest;
import com.example.spring.dtos.document.DocumentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {

    private static final String userSid = "e827e23c-960c-4c0a-aeb9-e5a25349ef9a";

    private static final String description = "new document";

    private static final String updatedDescription = "Updated document description";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createDocumentRecord_validRequest_expectCreated() throws Exception {

        DocumentRequest documentRequest = DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .description(description)
                .userId(UUID.fromString(userSid))
                .build();

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(documentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.documentType", is(DocumentType.JPG.getType())))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.sid", isA(String.class)))
                .andExpect(jsonPath("$.creationTime", isA(String.class)))
                .andExpect(jsonPath("$.updateTime", isA(String.class)));
    }

    @Test
    void createDocumentRecord_noDescription_expectCreated() throws Exception {

        DocumentRequest documentRequest = DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .userId(UUID.fromString(userSid))
                .build();

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(documentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.documentType", is(DocumentType.JPG.getType())))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.sid", isA(String.class)))
                .andExpect(jsonPath("$.creationTime", isA(String.class)))
                .andExpect(jsonPath("$.updateTime", isA(String.class)));
    }

    @Test
    void createDocumentRecord_inValidDescription_expectUnprocessableEntity() throws Exception {

        DocumentRequest documentRequest = DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .description("4")
                .userId(UUID.fromString(userSid))
                .build();

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(documentRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDocumentRecord_inValidDocumentType_expectUnprocessableEntity() throws Exception {

        DocumentRequest documentRequest = DocumentRequest.builder()
                .documentType("json")
                .description(description)
                .userId(UUID.fromString(userSid))
                .build();

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(documentRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDocumentRecord_inValidUserSid_expectUnprocessableEntity() throws Exception {

        DocumentRequest documentRequest = DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .description("4")
                .userId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(documentRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getDocument_whenCreated_expectOk() throws Exception {


        MvcResult result = mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(buildValidDocumentRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String documentSid = JsonPath.read(result.getResponse().getContentAsString(), "$.sid");

        this.mockMvc.perform(get("/api/documents/{document_id}", documentSid)
                .queryParam("userId", userSid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.documentType", is(DocumentType.JPG.getType())))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.sid", is(documentSid)))
                .andExpect(jsonPath("$.creationTime", isA(String.class)))
                .andExpect(jsonPath("$.updateTime", isA(String.class)));
    }

    @Test
    void getDocument_whenNoDocumentsPresent_expectNotFound() throws Exception {

        this.mockMvc.perform(get("/api/documents/{document_id}", UUID.randomUUID().toString())
                .queryParam("userId", userSid))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDocumentDescription_validRequest_expectOk() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(buildValidDocumentRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String documentSid = JsonPath.read(result.getResponse().getContentAsString(), "$.sid");

        DocumentRequest updateDocumentRequest = DocumentRequest.builder()
                .description(updatedDescription)
                .build();

        this.mockMvc.perform(patch("/api/documents/{document_id}", documentSid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(updateDocumentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.documentType", is(DocumentType.JPG.getType())))
                .andExpect(jsonPath("$.description", is(updatedDescription)))
                .andExpect(jsonPath("$.sid", is(documentSid)))
                .andExpect(jsonPath("$.creationTime", isA(String.class)))
                .andExpect(jsonPath("$.updateTime", isA(String.class)));
    }

    @Test
    void updateDocumentDescription_inValidRequest_expectUnprocessableEntity() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(buildValidDocumentRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String documentSid = JsonPath.read(result.getResponse().getContentAsString(), "$.sid");

        DocumentRequest updateDocumentRequest = DocumentRequest.builder()
                .userId(UUID.randomUUID())
                .build();

        this.mockMvc.perform(patch("/api/documents/{document_id}", documentSid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(updateDocumentRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateDocumentDescription_inDescriptionLength_expectUnprocessableEntity() throws Exception {


        MvcResult result = mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(buildValidDocumentRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String documentSid = JsonPath.read(result.getResponse().getContentAsString(), "$.sid");

        DocumentRequest updateDocumentRequest = DocumentRequest.builder()
                .description("0")
                .build();

        this.mockMvc.perform(patch("/api/documents/{document_id}", documentSid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(updateDocumentRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    private DocumentRequest buildValidDocumentRequest() {

        return DocumentRequest.builder()
                .documentType(DocumentType.JPG.getType())
                .description(description)
                .userId(UUID.fromString(userSid))
                .build();
    }
}
