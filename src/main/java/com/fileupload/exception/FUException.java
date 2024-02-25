package com.fileupload.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fileupload.model.ResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class FUException extends RuntimeException {

    private final ResultCode resultCode;
    private final HttpStatus httpStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> errors;
    public FUException(ResultCode resultCode) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
        this.httpStatus = resultCode.getHttpStatus();
        this.errors = null;
    }

    public FUException(ResultCode resultCode, List<String> errors) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
        this.httpStatus = resultCode.getHttpStatus();
        this.errors = errors;
    }
}
