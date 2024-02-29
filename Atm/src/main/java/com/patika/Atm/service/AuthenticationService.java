package com.patika.Atm.service;

import com.patika.Atm.dto.AuthRequest;
import com.patika.Atm.dto.AuthResponse;
import com.patika.Atm.dto.RegisterRequest;

public interface AuthenticationService {
    void register(RegisterRequest registerRequest);

    AuthResponse authenticate(AuthRequest authRequest);
}
