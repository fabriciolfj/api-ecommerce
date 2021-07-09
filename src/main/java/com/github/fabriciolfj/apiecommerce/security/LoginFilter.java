package com.github.fabriciolfj.apiecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabriciolfj.apiecommerce.model.SignInReq;
import com.github.fabriciolfj.apiecommerce.model.SignedInUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.MethodNotAllowedException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtManager tokenManager;
    private final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, JwtManager tokenManager, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.objectMapper = objectMapper;
        super.setFilterProcessesUrl("/api/v1/auth/token");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new MethodNotAllowedException(request.getMethod(), List.of(HttpMethod.POST));
        }

        try(InputStream is = request.getInputStream()) {
            var user = this.objectMapper.readValue(is, SignInReq.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        User principal = (User) auth.getPrincipal();
        String token = tokenManager.create(principal);
        SignedInUser user = new SignedInUser().username(principal.getUsername()).accessToken(token);
        res.setContentType(APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.getWriter().print(objectMapper.writeValueAsString(user));
        res.getWriter().flush();
    }
}
