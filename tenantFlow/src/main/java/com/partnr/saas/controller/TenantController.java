package com.partnr.saas.controller;

import com.partnr.saas.dto.request.UpdateTenantRequest;
import com.partnr.saas.service.TenantService;
import com.partnr.saas.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<ApiResponse<?>> getTenant(
            @PathVariable UUID tenantId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Tenant fetched", tenantService.getTenant(tenantId))
        );
    }

    @PutMapping("/{tenantId}")
    public ResponseEntity<ApiResponse<?>> updateTenant(
            @PathVariable UUID tenantId,
            @Valid @RequestBody UpdateTenantRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Tenant updated successfully",
                        tenantService.updateTenant(tenantId, request))
        );
    }
}
