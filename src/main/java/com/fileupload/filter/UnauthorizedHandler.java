package com.fileupload.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fileupload.model.ResultCode;
import com.fileupload.model.StandardResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResultCode invalidApikey = ResultCode.INVALID_APIKEY;
        response.setStatus(invalidApikey.getHttpStatus().value());
        PrintWriter out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        out.print(this.objectMapper.writeValueAsString(StandardResponse.createResponse(invalidApikey)));
        out.flush();
    }
}
