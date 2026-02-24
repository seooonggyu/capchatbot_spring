package com.thc.capchatbot.service.impl;

import com.thc.capchatbot.domain.Chatbot;
import com.thc.capchatbot.domain.UserSpace;
import com.thc.capchatbot.domain.UserSpaceStatus;
import com.thc.capchatbot.dto.ChatbotDto;
import com.thc.capchatbot.repository.ChatbotRepository;
import com.thc.capchatbot.repository.UserSpaceRepository;
import com.thc.capchatbot.service.ChatbotService;
import com.thc.capchatbot.client.RagChatbotClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final RagChatbotClient ragChatbotClient;
    private final UserSpaceRepository userSpaceRepository;
    private final ChatbotRepository chatbotRepository;

    @Override
    @Transactional
    public ChatbotDto.ChatResDto askChatbot(ChatbotDto.ChatReqDto param, Long reqUserId) {
        log.info("[ChatbotRequest] userId: {}, spaceId: {}, question: {}", reqUserId, param.getSpaceId(), param.getQuestion());

        String query = param.getQuestion();
        if (query == null || query.isBlank()) {
            log.warn("[ChatbotRequest] Failed: Question is blank. userId: {}", reqUserId);
            throw new IllegalArgumentException("question is required");
        }
        if (reqUserId == null) {
            log.warn("[ChatbotRequest] Failed: reqUserId is null.");
            throw new IllegalArgumentException("reqUserId is required");
        }

        Long spaceId = param.getSpaceId();
        System.out.println("spaceId = " + spaceId);
        UserSpace userSpace = userSpaceRepository.findFirstByUserIdAndSpaceIdAndStatus(reqUserId, spaceId, UserSpaceStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.error("[ChatbotRequest] Active userSpace not found! userId: {}, spaceId: {}", reqUserId, spaceId);
                    return new IllegalStateException("Active userSpace not found for userId=" + reqUserId);
                });


        try {
            String answer = ragChatbotClient.ask(param, spaceId);

            // 3) chat history 저장
            Long userSpaceId = userSpace.getId();
            Chatbot saved = chatbotRepository.save(Chatbot.of(query, answer, userSpaceId));
            log.info("[ChatbotResponse] Answer saved to DB. chatbotId: {}, userSpaceId: {}", saved.getId(), userSpaceId);

            // 4) 응답
            return ChatbotDto.ChatResDto.builder()
                    .answer(answer)
                    .build();
        } catch (Exception e) {
            log.error("[ChatbotRequest] Python API Communication Error: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Override
    @Async
    public void ingestRequest(Long spaceId, String filePath) {
        log.info("[IngestRequest] Starting async ingest. spaceId: {}, path: {}", spaceId, filePath);
        try {
            ragChatbotClient.ingest(spaceId, filePath);
            // 성공 로그
            log.info("[IngestRequest] Ingest request sent successfully. spaceId={}, filePath={}", spaceId, filePath);
        } catch (Exception e) {
            // 실패 로그
            log.error("[IngestRequest] Python ingest request failed! spaceId={}, filePath={}", spaceId, filePath, e);
        }
    }

    @Override
    public List<ChatbotDto.HistoryResDto> getHistory(Long spaceId, Long reqUserId) {
        log.info("[ChatHistory] Fetching history for userId: {}, spaceId: {}", reqUserId, spaceId);

        UserSpace userSpace = userSpaceRepository.findFirstByUserIdAndSpaceIdAndStatus(reqUserId, spaceId, UserSpaceStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.error("[ChatHistory] Active userSpace not found! userId: {}, spaceId: {}", reqUserId, spaceId);
                    return new IllegalStateException("Active userSpace not found for userId=" + reqUserId);
                });

        List<Chatbot> chatHistory = chatbotRepository.findByUserSpaceIdOrderByCreatedAtAsc(userSpace.getId());
        log.info("[ChatHistory] Found {} records for userSpaceId: {}", chatHistory.size(), userSpace.getId());

        return chatHistory.stream().map(chat -> chat.toHistoryResDto()).collect(Collectors.toList());
    }


}
