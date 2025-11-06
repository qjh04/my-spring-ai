package cn.itcast.service.impl;

import cn.itcast.domain.dto.ChatDTO;
import cn.itcast.service.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatClient chatClient;

    @Override
    public String chat(String question) {
        String content = this.chatClient
                .prompt()
                .user(question)
                .call()
                .content();
        log.info("content: {}", content);
        return content;
    }

    @Override
    public Flux<String> chatStream(ChatDTO chatDTO) {
        return this.chatClient
                .prompt()
                .user(chatDTO.getQuestion())
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatDTO.getSessionId()))
                .stream()
                .content();
    }
}
