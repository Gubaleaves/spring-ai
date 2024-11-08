package com.example.openai.controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ContextController {

    private final ChatModel chatModel;

    // 历史消息列表
    static List<Message> historyMessage = new ArrayList<>();

    // 历史消息列表的最大长度
    static int maxLen = 10;

    public ContextController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/context")
    public String context(String prompt) {
        // 用户输入的文本是UserMessage
        historyMessage.add(new UserMessage(prompt));
        // 发给AI前对历史消息对列的长度进行检查
        if(historyMessage.size() > maxLen){
                historyMessage = historyMessage.subList(historyMessage.size()-maxLen-1,historyMessage.size());
            }
            // 获取AssistantMessage
            ChatResponse chatResponse = chatModel.call(new Prompt(historyMessage));
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 将AI回复的消息放到历史消息列表中
            historyMessage.add(assistantMessage);
            return assistantMessage.getContent();
        }
}
