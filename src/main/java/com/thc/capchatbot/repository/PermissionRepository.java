package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
