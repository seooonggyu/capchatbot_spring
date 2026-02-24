package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
