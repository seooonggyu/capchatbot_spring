package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByContent(String content);
    List<RefreshToken> findByUserId(Long userId);
}
