package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Permissiondetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissiondetailRepository extends JpaRepository<Permissiondetail, Long> {
    Permissiondetail findByPermissionIdAndTargetAndFunc(Long permissionId, String target, Integer func);
}
