package com.aakarsh.springai.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/openai")
public class AIChatController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/interesting-starter-prompt.st")
    private Resource resource;

    @Autowired
    public AIChatController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/prompt")
    public String completion(@RequestParam(value = "topic", defaultValue = "Java Spring Project") String topic,
                             @RequestParam(value = "number", defaultValue = "30") String number,
                             @RequestParam(value = "age", defaultValue = "15") String age) {

        PromptTemplate promptTemplate = new PromptTemplate(resource);

        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "number", number, "age", age));

        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}
