package cn.itcast.service.impl;

import cn.itcast.service.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
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
    public Flux<String> chatStream(String question) {
        return this.chatClient
                .prompt()
                .user(question)
                .stream()
                .content();
    }
}
