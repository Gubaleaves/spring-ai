package com.example.openai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionController {

    private final ChatModel chatModel;

    public FunctionController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @RequestMapping("/func")
    public String funcFaq(@RequestParam(value = "message") String message) {
        ChatResponse response = chatModel.call(
                new Prompt(message, OpenAiChatOptions.builder().withFunction("currentWeather").build()));
        return response.getResult().getOutput().getContent();
    }
}
