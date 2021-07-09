package com.github.fabriciolfj.apiecommerce.security;

import static com.github.fabriciolfj.apiecommerce.security.Constants.EXPIRATION_TIME;
import static com.github.fabriciolfj.apiecommerce.security.Constants.ROLE_CLAIM;
import static com.github.fabriciolfj.apiecommerce.security.Constants.SECRET_KEY;
import static java.util.stream.Collectors.toList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log4j2
@Component
public class JwtManager {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;
    @Value("${app.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;
    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;
    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    public String create(UserDetails principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("Modern API Development with Spring and Spring Boot")
                .withSubject(principal.getUsername())
                .withClaim(ROLE_CLAIM,
                        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(toList()))
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
                //.sign(Algorithm.RSA256(publicKey, privateKey));
    }

    @PostConstruct
    public void init() {
        this.publicKey = jwtValidationKey(keyStore());
        this.privateKey= jwtSigningKey(keyStore());
    }

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Unable to load keystore: {}", keyStorePath, e);
        }

        throw new IllegalArgumentException("Unable to load keystore");
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Unable to load private key from keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("Unable to load private key");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();
            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException e) {
            log.error("Unable to load private key from keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("Unable to load RSA public key");
    }
}
