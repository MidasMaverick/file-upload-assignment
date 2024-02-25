package com.fileupload.filter;

import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService {

    @Value("${api.key.header}")
    private String apiKeyHeader;

    @Value("${api.key}")
    private String apiKey;

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String xApiKey = request.getHeader(apiKeyHeader);
        if (xApiKey == null || !xApiKey.equals(apiKey)) {
            return Optional.empty();
        }

        return Optional.of(new ApiKeyAuthentication(xApiKey, AuthorityUtils.NO_AUTHORITIES));
    }
}
