package com.mouli.tenantFlow.service;

import com.mouli.tenantFlow.entity.AuditLog;
import com.mouli.tenantFlow.repository.AuditLogRepository;
import com.mouli.tenantFlow.security.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            String userEmail,
            String action,
            String entityType,
            String entityId
    ) {
        AuditLog log = new AuditLog();
        log.setTenantId(TenantContext.getTenantId());
        log.setUserEmail(userEmail);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setTimestamp(Instant.now());

        auditLogRepository.save(log);
    }
}
