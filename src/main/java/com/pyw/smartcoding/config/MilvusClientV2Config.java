package com.pyw.smartcoding.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MilvusClientV2Config {

    @Value("${milvus.host}")
    private String host;

    @Bean
    public MilvusClientV2 milvusClientV2() {

        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(host)
//                .token("root:Milvus")
                .build();
        return new MilvusClientV2(connectConfig);
    }

//    @Bean
//    public EmbeddingStore<TextSegment> embeddingStore() {
//        return MilvusEmbeddingStore.builder()
//                .uri(host) // Milvus 服务地址
//                .collectionName("my_test_collection") // 集合名称
//                .dimension(256) // 向量维度，与 qwenEmbeddingModel 的输出维度一致
//                .build();
//    }

}
