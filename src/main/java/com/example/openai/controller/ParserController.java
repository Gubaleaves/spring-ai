package com.example.openai.controller;

import com.example.openai.vo.Movies;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ParserController {

    private final ChatModel chatModel;

    public ParserController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/list_movie")
    public List<String> getHistoryMovie(@RequestParam(value = "category", defaultValue = "科幻") String category) {
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());
        String message = " 列出你认为世界上最好看的十部{category}电影，带上他们上映的年份，并给出推荐理由， {format} ";
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("category", category, "format", listOutputConverter.getFormat()));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        return listOutputConverter.convert(this.chatModel.call(prompt).getResult().getOutput().getContent());
    }

    @GetMapping("/map_movie")
    public Map<String, Object> getHistoryMovieByMap(@RequestParam(value = "category", defaultValue = "科幻") String category) {
        MapOutputConverter mapOutputConverter = new MapOutputConverter();
        String message = " 列出你认为世界上最好看的十部{category}电影，带上他们上映的年份，并给出推荐理由， {format} ";
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("category", category, "format", mapOutputConverter.getFormat()));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        return mapOutputConverter.convert(this.chatModel.call(prompt).getResult().getOutput().getContent());
    }

    @GetMapping("/bean_movie")
    public Movies getHistoryMovieByBean(@RequestParam(value = "category", defaultValue = "科幻") String category) {
        BeanOutputConverter<Movies> beanOutputConverter = new BeanOutputConverter<>(Movies.class);
        String message = " 列出你认为世界上最好看的十部{category}电影，带上他们上映的年份，并给出推荐理由， {format} ";
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("category", category, "format", beanOutputConverter.getFormat()));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        return beanOutputConverter.convert(this.chatModel.call(prompt).getResult().getOutput().getContent());
    }
}
