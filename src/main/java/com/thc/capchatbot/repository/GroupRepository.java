package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
