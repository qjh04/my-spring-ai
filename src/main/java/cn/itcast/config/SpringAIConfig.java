package cn.itcast.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIConfig {
    /**
     * 创建一个 chat 客户端
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("你是一个热心可爱的聊天机器人，你叫小团团")
                .build();
    }
}
