package com.mouli.tenantFlow.repository;

import com.mouli.tenantFlow.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByTenantId(UUID tenantId);
}
