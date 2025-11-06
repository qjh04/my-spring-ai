package cn.itcast.controller;

import cn.itcast.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public String chat(@RequestBody String question) {
        log.info("question: {}", question);
        return this.chatService.chat(question);
    }

    @PostMapping("/stream")
    public Flux<String> chatStream(@RequestBody String question) {
        log.info("question: {}", question);
        return this.chatService.chatStream(question);
    }

}
