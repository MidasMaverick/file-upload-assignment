package com.fileupload.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {
    SUCCESS("0", "success", HttpStatus.OK),
    INVALID_APIKEY("4001", "Invalid Api Key", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("4002", "Invalid request", HttpStatus.BAD_REQUEST),
    UNABLE_TO_PROCESS("5001", "Unable to process", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("9999", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String desc;
    private final HttpStatus httpStatus;

    ResultCode(String code, String desc, HttpStatus httpStatus) {
        this.code = code;
        this.desc = desc;
        this.httpStatus = httpStatus;
    }
}
