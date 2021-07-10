package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.UserApi;
import com.github.fabriciolfj.apiecommerce.model.RefreshToken;
import com.github.fabriciolfj.apiecommerce.model.SignInReq;
import com.github.fabriciolfj.apiecommerce.model.SignedInUser;
import com.github.fabriciolfj.apiecommerce.model.User;
import com.github.fabriciolfj.apiecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController implements UserApi {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<SignedInUser> signIn(@Valid @RequestBody SignInReq signInReq) {
        var userEntity = userService.findUserByUserName(signInReq.getUsername());

        if (passwordEncoder.matches(signInReq.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.ok(userService.getSignedInUser(userEntity));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<Void> signOut(RefreshToken refreshToken) {
        userService.removeRefreshToken(refreshToken);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<SignedInUser> signUp(User user) {
        var result = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<SignedInUser> getAccessToken(RefreshToken refreshToken) {
        return ResponseEntity.ok(userService.getAccessToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Fail")));
    }
}
