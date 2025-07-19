package com.pyw.smartcoding;

import com.pyw.smartcoding.processor.AiSmartCoderService;
import com.pyw.smartcoding.processor.SmartCoder;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmartCodingApplicationTests {

    @Resource
    private SmartCoder smartCoder;

    @Resource
    private AiSmartCoderService aiSmartCoderService;

    @Test
    void contextLoads() {
        smartCoder.chat("写一个java程序，打印1到1000的偶数");
    }

    @Test
    void testChatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://www.codefather.cn/logo.png")
        );
        smartCoder.chatWithMessage(userMessage);
    }

    @Test
    void chat(){
        String result = aiSmartCoderService.chat("你好，我是程序员鱼皮");
        System.out.println(result);
    }

}
