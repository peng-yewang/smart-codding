package com.pyw.smartcoding.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import java.util.List;

//@AiService
public interface AiSmartCoderService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String message);

    String chat(@MemoryId int memoryId, @UserMessage String message);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggestionList){}

    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);
}
