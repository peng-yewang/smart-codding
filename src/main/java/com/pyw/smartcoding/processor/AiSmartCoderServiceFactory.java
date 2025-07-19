package com.pyw.smartcoding.processor;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiSmartCoderServiceFactory {

    @Resource
    private ChatModel qwenChatModel;

    @Bean
    public AiSmartCoderService aiSmartCoderService() {
        return AiServices.create(AiSmartCoderService.class, qwenChatModel);
    }

}
