package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Role;
import com.thc.capchatbot.domain.UserSpace;
import com.thc.capchatbot.domain.UserSpaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSpaceRepository extends JpaRepository<UserSpace, Long> {
    Optional<UserSpace> findByUserIdAndSpaceId(Long userId, Long spaceId);

    Optional<UserSpace> findByUserIdAndSpaceIdAndRole(Long id, Long spaceId, Role role);

    List<UserSpace> findAllBySpaceIdAndRoleAndStatus(Long spaceId, Role role, UserSpaceStatus status);

    Optional<UserSpace> findFirstByUserIdAndSpaceIdAndStatus(Long userId, Long spaceId, UserSpaceStatus status);
}
