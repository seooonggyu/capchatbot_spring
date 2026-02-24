package com.thc.capchatbot.repository;

import com.thc.capchatbot.domain.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatbotRepository extends JpaRepository<Chatbot, Long>  {
    List<Chatbot> findByUserSpaceIdOrderByCreatedAtAsc(Long userSpaceId);
}
