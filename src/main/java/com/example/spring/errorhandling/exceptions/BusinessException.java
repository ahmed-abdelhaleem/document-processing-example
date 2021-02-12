package com.example.spring.errorhandling.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BusinessException extends Exception{

    protected final String customErrorMessage;

    protected final Optional<HttpStatus> httpStatus;

}
