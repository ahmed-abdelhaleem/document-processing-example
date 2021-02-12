package com.example.spring.errorhandling.dtos;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericErrorResponse {

    private String message;

    private String description;
}
