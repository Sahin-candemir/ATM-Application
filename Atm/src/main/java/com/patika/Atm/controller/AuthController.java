package com.patika.Atm.controller;

import com.patika.Atm.dto.AuthRequest;
import com.patika.Atm.dto.AuthResponse;
import com.patika.Atm.dto.RegisterRequest;
import com.patika.Atm.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest){
        authenticationService.register(registerRequest);
        return new ResponseEntity<>("User registered success.", HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest){
        return new ResponseEntity<>(authenticationService.authenticate(authRequest), HttpStatus.OK);
    }
}
