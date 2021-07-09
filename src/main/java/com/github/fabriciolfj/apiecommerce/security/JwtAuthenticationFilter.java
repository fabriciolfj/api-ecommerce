package com.github.fabriciolfj.apiecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.fabriciolfj.apiecommerce.security.Constants.AUTHORIZATION;
import static com.github.fabriciolfj.apiecommerce.security.Constants.ROLE_CLAIM;
import static com.github.fabriciolfj.apiecommerce.security.Constants.SECRET_KEY;
import static com.github.fabriciolfj.apiecommerce.security.Constants.TOKEN_PREFIX;

@Configuration
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var header = request.getHeader("Authorization");

        if (Objects.isNull(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        Optional<UsernamePasswordAuthenticationToken> authentication = getAuthentication(request);
        authentication.ifPresentOrElse(e -> SecurityContextHolder.getContext().setAuthentication(e),
                SecurityContextHolder::clearContext);
        chain.doFilter(request, response);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (Objects.nonNull(token)) {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String user = jwt.getSubject();
            if (Objects.nonNull(user)) {
                return Optional.of(new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList()));
            }
        }
        return Optional.empty();
    }
}
