package com.github.fabriciolfj.apiecommerce.security;

import static com.github.fabriciolfj.apiecommerce.security.Constants.TOKEN_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.fabriciolfj.apiecommerce.exceptions.ErrorCode;
import com.github.fabriciolfj.apiecommerce.exceptions.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private ObjectMapper mapper;

    public ApiAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res, AuthenticationException e)
            throws IOException, ServletException {
        LOG.info("exception = " + e);
        LOG.info("exception.getCause() = " + e.getCause());
        String errorMsg = "";
        if (e instanceof InsufficientAuthenticationException) {
            errorMsg = e.getMessage();
        } else {
            errorMsg = ErrorCode.UNAUTHORIZED.getErrMsgKey();
        }
        var error = ErrorUtils
                .createError(errorMsg, ErrorCode.UNAUTHORIZED.getErrCode(),
                        HttpStatus.UNAUTHORIZED.value());
        res.setContentType(APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        OutputStream out = res.getOutputStream();
        mapper.writeValue(out, error);
        out.flush();
    }
}
