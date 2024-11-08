package com.example.openai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RagController {

    private final ChatModel chatModel;

    private final VectorStore vectorStore;

    @Value("classpath:/prompts/rag.st")
    private Resource ragPromptTemplate;

    public RagController(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/rag")
    public String faq(@RequestParam(value = "message", defaultValue = "抗日战争为什么是持久战？") String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(
                ragPromptTemplate, Map.of("input", message, "documents", String.join("\n", contentList)));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}
