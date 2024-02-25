package com.fileupload.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StandardResponse<T> {

    private ResponseCode responseCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T responseBody;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ResponseCode {
        private String code;
        private String desc;
    }

    private StandardResponse(ResponseCode responseCode, T responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    private StandardResponse(ResponseCode responseCode, List<String> errors) {
        this.responseCode = responseCode;
        this.errors = errors;
    }

    public static <T> StandardResponse<T> createResponse(ResultCode resultCode) {
        ResponseCode responseCode = new ResponseCode(resultCode.getCode(), resultCode.getDesc());
        return new StandardResponse<>(responseCode, null);
    }

    public static <T> StandardResponse<T> createResponse(ResultCode resultCode, T responseBody) {
        ResponseCode responseCode = new ResponseCode(resultCode.getCode(), resultCode.getDesc());
        return new StandardResponse<>(responseCode, responseBody);
    }

    public static <T> StandardResponse<T> createResponse(ResultCode resultCode, List<String> errors) {
        ResponseCode responseCode = new ResponseCode(resultCode.getCode(), resultCode.getDesc());
        return new StandardResponse<>(responseCode, errors);
    }
}
