package com.example.openai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StuffController {

    private final ChatModel chatModel;

    @Value("classpath:/prompts/stuff.st")
    private Resource promptResource;

    @Value("classpath:/docs/stuff.txt")
    private Resource stuffResource;

    public StuffController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @RequestMapping("/stuff")
    public String getSports(@RequestParam(value = "message", defaultValue = "2024年夏季奥运会有哪些运动项目") String message,
                            @RequestParam(value = "stuff", defaultValue = "false") boolean stuff) {
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);

        Map<String, Object> map = new HashMap<>();
        map.put("question", message);

        if(stuff) {
            map.put("context", stuffResource);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}
