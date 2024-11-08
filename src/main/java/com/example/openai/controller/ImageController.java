package com.example.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ChatClient client;

    private final OpenAiImageModel imageModel;

    public ImageController(ChatClient.Builder chatclientBuilder, OpenAiImageModel imageModel) {
        this.client = chatclientBuilder.build();
        this.imageModel = imageModel;
    }

    @GetMapping("/describe")
    public String describeImage() {
        Resource resource = new ClassPathResource("images/1.png");

        UserMessage userMessage = new UserMessage("你是一个优秀的设计师和图像学家，你可以从专业的角度描述下你看到的图片吗？",
                new Media(MimeTypeUtils.IMAGE_PNG, resource));

        return client.prompt(new Prompt(userMessage)).call().chatResponse().getResult().getOutput().getContent();
    }

    @GetMapping("/generate")
    public String createImage(@RequestParam(value = "prompt") String prompt){
        ImagePrompt imagePrompt = new ImagePrompt(prompt,
                OpenAiImageOptions.builder()
                        .withResponseFormat("url")
                        .withHeight(1024)
                        .withWidth(1024)
                        .build());
        ImageGeneration generation = imageModel.call(imagePrompt).getResult();
        String url = generation.getOutput().getUrl();

        return String.format("<img src='%s' alt='%s'>", url, prompt);
    }
}
