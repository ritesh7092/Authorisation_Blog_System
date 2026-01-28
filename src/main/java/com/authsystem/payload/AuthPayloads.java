package com.authsystem.payload;

import java.util.List;

public class AuthPayloads {
    public record LoginRequest(String email, String password) {}

    public record SignupRequest(String name, String email, String password, String role) {}

    public record JwtResponse(String token, Long id, String name, String email, String role) {}

    public record MessageResponse(String message) {}
}
