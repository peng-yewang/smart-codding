package com.pyw.smartcoding.processor;

import dev.langchain4j.service.SystemMessage;

public interface AiSmartCoderService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String message);

}
