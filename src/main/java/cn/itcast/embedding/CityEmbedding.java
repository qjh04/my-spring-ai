package cn.itcast.embedding;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将城市名称转换为向量，存入向量数据库中
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CityEmbedding {

    /**
     * 向量数据库
     */
    private final VectorStore vectorStore;

    /**
     * 城市名称文件路径
     * 注意：@Value 注解只能用于字段注入，不能用于构造函数注入
     */
    @Value("classpath:city2id.txt")
    private Resource resource;

    /**
     * 初始化向量数据库
     */
    @PostConstruct
    public void embedding() {
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("embedding", "city2id.txt");

        List<Document> documents = textReader.get();
        //参数分别是：默认分块大小、最小分块字符数、最小向量化长度（太小的忽略）、最大分块数量、不保留分隔符（\n啥的）
        TextSplitter textSplitter = new TokenTextSplitter(200, 100, 5, 10000, false);
        List<Document> splitDocuments = textSplitter.apply(documents);

        // 3. 将处理后的文档向量化并存入向量存储
        this.vectorStore.add(splitDocuments);
        log.info("数据写入向量库成功，数据条数：{}", splitDocuments.size());
    }

}
