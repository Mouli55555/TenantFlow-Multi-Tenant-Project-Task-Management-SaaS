package com.partnr.saas.controller;

import com.partnr.saas.dto.request.LoginRequest;
import com.partnr.saas.dto.request.RegisterTenantRequest;
import com.partnr.saas.dto.response.AuthResponse;
import com.partnr.saas.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-tenant")
    public ResponseEntity<?> registerTenant(
            @Valid @RequestBody RegisterTenantRequest request
    ) {
        authService.registerTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "success", true,
                        "message", "Tenant registered successfully"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }
}
