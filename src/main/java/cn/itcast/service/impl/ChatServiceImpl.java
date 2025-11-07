package cn.itcast.service.impl;

import cn.itcast.domain.dto.ChatDTO;
import cn.itcast.service.ChatService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

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

        SearchRequest searchRequest = SearchRequest.builder()
                .query(chatDTO.getQuestion())
                .topK(2)
                .build();

        return this.chatClient
                .prompt()
                .user(chatDTO.getQuestion())
                .advisors(a -> a
                        .advisors(new QuestionAnswerAdvisor(vectorStore, searchRequest))
                        .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatDTO.getSessionId()))
                .stream()
                .content();
    }
}
