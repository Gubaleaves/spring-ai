package com.example.openai.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Value("classpath:/prompts/prompts.st")
    private Resource promptsResource;

    // 简单对话
    @GetMapping("/hello")
    public String Hello(@RequestParam(value = "message", defaultValue = "Hello AI, How are you?") String message) {
        return this.chatModel.call(message);
    }

    // 带提示词
    @GetMapping("/master")
    public String master(@RequestParam(value = "message") String message) {
        var system = new SystemMessage("""
                你是一个算命大师，名叫陈玉楼，负责为客户解答命理相关的内容；
                你从不说自己是一个人工智能助手或AI，而是以老夫、老朽等自称。
                你需要依赖用户的出生年月来进行算命，当初次和用户对话的时候，你会先问用户的姓名和出生年月，以便之后使用。
                以下是你常说的一些口头禅：
                1.“信则有不信则无，你的生辰八字是什么？算的准就给钱，不准分文不要。”
                2.“命运需要自己改变，想说就说想做就做，否则会后悔的。”
                3.“福自天来，事不须求。年当大有为，富丽好时光。”
                4.“时逢春回日，百花正及时。得人轻借力，便是运通时。”
                """);
        Prompt prompt = new Prompt(List.of(system, new UserMessage(message)));
         return this.chatModel.call(prompt).getResult().getOutput().getContent();
    }

    // 带待定参数
    @GetMapping("/popular")
    public String getHistoryMovie(@RequestParam(value = "category", defaultValue = "科幻") String category) {
        String message = "列出你认为世界上最好看的十部{category}电影，带上他们上映的年份";
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("category", category));
        return this.chatModel.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/popular/st")
    public String getHistoryMovieByST(@RequestParam(value = "category", defaultValue = "科幻") String category) {
        PromptTemplate promptTemplate = new PromptTemplate(promptsResource);
        Prompt prompt = promptTemplate.create(Map.of("category", category));
        return this.chatModel.call(prompt).getResult().getOutput().getContent();
    }
}
