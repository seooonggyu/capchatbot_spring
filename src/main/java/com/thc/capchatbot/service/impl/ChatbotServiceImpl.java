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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final RagChatbotClient ragChatbotClient;
    private final UserSpaceRepository userSpaceRepository;
    private final ChatbotRepository chatbotRepository;

    @Override
    @Transactional
    public ChatbotDto.ChatResDto askChatbot(ChatbotDto.ChatReqDto param, Long reqUserId) {

        String query = param.getQuestion();
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("question is required");
        }
        if (reqUserId == null) {
            throw new IllegalArgumentException("reqUserId is required");
        }

        Long spaceId = param.getSpaceId();
        System.out.println("spaceId = " + spaceId);
        UserSpace userSpace = userSpaceRepository.findFirstByUserIdAndSpaceIdAndStatus(reqUserId, spaceId, UserSpaceStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active userSpace not found for userId=" + reqUserId));


        String answer = ragChatbotClient.ask(param, spaceId);

        // 3) 로그 저장
        Long userSpaceId = userSpace.getId();
        Chatbot saved = chatbotRepository.save(Chatbot.of(query, answer, userSpaceId));

        // 4) 응답
        return ChatbotDto.ChatResDto.builder()
                .answer(answer)
                .build();
    }

    @Override
    @Async
    public void ingestRequest(Long spaceId, String filePath) {
        try {
            ragChatbotClient.ingest(spaceId, filePath);
            // 성공 로그 log.info("Ingest request sent successfully. spaceId={}, filePath={}", spaceId, filePath);
        } catch (Exception e) {
            // 실패 로그 log.error("Python ingest request failed! spaceId={}, filePath={}", spaceId, filePath, e);
        }
    }

    @Override
    public List<ChatbotDto.HistoryResDto> getHistory(Long spaceId, Long reqUserId) {
        UserSpace userSpace = userSpaceRepository.findFirstByUserIdAndSpaceIdAndStatus(reqUserId, spaceId, UserSpaceStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active userSpace not found for userId=" + reqUserId));

        List<Chatbot> chatHistory = chatbotRepository.findByUserSpaceIdOrderByCreatedAtAsc(userSpace.getId());
        return chatHistory.stream().map(chat -> chat.toHistoryResDto()).collect(Collectors.toList());
    }


}
