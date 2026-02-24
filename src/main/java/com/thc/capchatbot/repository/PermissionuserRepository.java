package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Permissionuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionuserRepository extends JpaRepository<Permissionuser, Long> {
    Permissionuser findByPermissionIdAndUserId(Long permissionId, Long userId);
}