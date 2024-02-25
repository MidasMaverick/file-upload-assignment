package com.fileupload.exception;

import com.fileupload.model.ResultCode;
import com.fileupload.model.StandardResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, errors);

        return handleExceptionInternal(ex, response, headers, ResultCode.INVALID_REQUEST.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(builder::append);

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, List.of(builder.toString()));

        return handleExceptionInternal(ex, response, headers, ResultCode.INVALID_REQUEST.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, List.of(error));

        return handleExceptionInternal(ex, response, headers, ResultCode.INVALID_REQUEST.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getRequestPartName() + " parameter is missing";

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, List.of(error));

        return handleExceptionInternal(ex, response, headers, ResultCode.INVALID_REQUEST.getHttpStatus(), request);
    }

    @ExceptionHandler(FUException.class)
    public ResponseEntity<StandardResponse<Object>> handleFileUploadException(FUException ex) {

        StandardResponse<Object> response = StandardResponse.createResponse(ex.getResultCode(), ex.getErrors());

        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<StandardResponse<Object>> handleFileUploadException(MultipartException ex) {
        String message = ex.getMessage();

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, List.of(message));

        return new ResponseEntity<>(response, ResultCode.INVALID_REQUEST.getHttpStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            NodeImpl leafNode = ((PathImpl) violation.getPropertyPath()).getLeafNode();
            ElementKind kind = leafNode.getKind();
            String fieldName = null;
            if (kind.equals(ElementKind.CONTAINER_ELEMENT)) {
                fieldName = leafNode.getParent().asString();
            } else {
                for (Path.Node node : violation.getPropertyPath()) {
                    fieldName = node.getName();
                }
            }
            errors.add(fieldName + " " + violation.getMessage());
        }

        StandardResponse<Object> response = StandardResponse.createResponse(ResultCode.INVALID_REQUEST, errors);

        return new ResponseEntity<>(response, ResultCode.INVALID_REQUEST.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> handleException(Exception ex) {
        return new ResponseEntity<>(StandardResponse.createResponse(ResultCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
