package com.partnr.saas.repository;

import com.partnr.saas.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    long countByTenantId(UUID tenantId);
    List<Task> findByTenantIdAndProjectId(UUID tenantId, UUID projectId);

    Optional<Task> findByIdAndTenantId(UUID id, UUID tenantId);
}
