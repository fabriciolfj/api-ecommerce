package com.github.fabriciolfj.apiecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabriciolfj.apiecommerce.model.SignInReq;
import com.github.fabriciolfj.apiecommerce.model.SignedInUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@TestConfiguration
public class AuthClient {

  private TestRestTemplate restTemplate;
  private ObjectMapper objectMapper;

  public AuthClient(TestRestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public SignedInUser login(String username, String password) {
    SignInReq signInReq = new SignInReq().username(username).password(password);
    return restTemplate
        .execute("/api/v1/auth/token",
            HttpMethod.POST,
            request -> {
              objectMapper.writeValue(request.getBody(), signInReq);
              request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
              request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            },
            response -> objectMapper.readValue(response.getBody(), SignedInUser.class)
        );
  }
}
