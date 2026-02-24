package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
