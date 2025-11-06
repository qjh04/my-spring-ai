package cn.itcast.service;

import cn.itcast.domain.dto.ChatDTO;
import reactor.core.publisher.Flux;

public interface ChatService {

    /**
     * 普通聊天接口
     */
    String chat(String question);

    /**
     * 流式输出聊天接口
     */
    Flux<String> chatStream(ChatDTO chatDTO);
}
