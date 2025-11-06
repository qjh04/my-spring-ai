package cn.itcast.config;

import cn.itcast.constant.Constant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringAIConfig {
    /**
     * 创建一个 chat 客户端
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 Advisor simpleLoggerAdvisor,
                                 Advisor messageChatMemoryAdvisor,
                                 Advisor safeGuardAdvisor) {
        return builder
                .defaultSystem(Constant.SYS_ROLE)   // 系统 prompt
                .defaultAdvisors(
                        simpleLoggerAdvisor,        // 日志
                        messageChatMemoryAdvisor,   // 聊天记录
                        safeGuardAdvisor            // 安全过滤
                )
                .build();
    }

    /**
     * chat_model 日志 Advisor
     */
    @Bean
    public Advisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    /**
     * 内存记忆
     */
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    /**
     * 基于内存记忆的 聊天记录 Advisor
     */
    @Bean
    public Advisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return new MessageChatMemoryAdvisor(chatMemory);
    }

    /**
     * 敏感词过滤 Advisor
     */
    @Bean
    public Advisor safeGuardAdvisor() {
        // 敏感词列表（示例数据，建议实际使用时从配置文件或数据库读取）
        List<String> sensitiveWords = List.of("敏感词1", "敏感词2");
        // 创建安全防护Advisor，参数依次为：敏感词库、违规提示语、advisor处理优先级，数字越小越优先
        return new SafeGuardAdvisor(
                sensitiveWords,
                "敏感词提示：请勿输入敏感词！",
                Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER
        );
    }
}
