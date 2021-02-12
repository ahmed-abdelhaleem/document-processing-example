package com.example.spring.errorhandling.handlers;

import com.example.spring.errorhandling.dtos.GenericErrorResponse;
import com.example.spring.errorhandling.exceptions.BusinessException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice(basePackages = "com.example.documents.document-processing.controllers")
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GenericErrorResponse> handleBusinessException(BusinessException exception) {

        GenericErrorResponse genericErrorResponse = getGenericErrorResponse(exception);

        return exception.getHttpStatus()
                .map(httpStatus -> new ResponseEntity<>(genericErrorResponse, httpStatus))
                .orElse(new ResponseEntity<>(genericErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }


    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers, org.springframework.http.HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = Optional.ofNullable(ex.getFieldError())
                .map(fieldError -> new StringBuilder("Rejected Field ").append(fieldError.getField()).append(" With Value ").append(fieldError.getRejectedValue()))
                .map(StringBuilder::toString).orElse(ex.getMessage());

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex, message));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity.unprocessableEntity().body(getGenericErrorResponse(ex));
    }

    private GenericErrorResponse getGenericErrorResponse(final Exception exception) {

        return GenericErrorResponse.builder()
                .message(exception.getMessage())
                .description(exception.getMessage())
                .build();
    }

    private GenericErrorResponse getGenericErrorResponse(final Exception exception, final String message) {

        return GenericErrorResponse.builder()
                .message(message)
                .description(exception.getMessage())
                .build();
    }

    private GenericErrorResponse getGenericErrorResponse(final BusinessException exception) {

        return GenericErrorResponse.builder()
                .message(exception.getCustomErrorMessage())
                .description(Optional.ofNullable(exception.getMessage()).orElse(exception.getCustomErrorMessage()))
                .build();
    }
}
