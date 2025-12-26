package com.partnr.saas.controller;

import com.partnr.saas.dto.request.CreateUserRequest;
import com.partnr.saas.dto.request.UpdateUserRequest;
import com.partnr.saas.service.UserService;
import com.partnr.saas.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ===================== ADD USER ===================== */

    @PostMapping("/tenants/{tenantId}/users")
    public ResponseEntity<ApiResponse<?>> addUser(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateUserRequest request
    ) {
        userService.createUser(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", null));
    }

    /* ===================== LIST USERS ===================== */

    @GetMapping("/tenants/{tenantId}/users")
    public ResponseEntity<ApiResponse<?>> listUsers(
            @PathVariable UUID tenantId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Users fetched",
                        userService.listUsers(tenantId)
                )
        );
    }

    /* ===================== UPDATE USER ===================== */

    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRequest request
    ) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully", null)
        );
    }

    /* ===================== DELETE USER ===================== */

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @PathVariable UUID userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully", null)
        );
    }
}
