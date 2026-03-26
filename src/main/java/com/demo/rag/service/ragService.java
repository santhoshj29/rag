package com.demo.rag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ragService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    private static final Logger logger = LoggerFactory.getLogger(ragService.class);

    @Value("classpath:/prompt/rag-prompt-template.st")
    private Resource faq;

    @Autowired
    public ragService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String faq(String message){

        logger.info("Received message: " + message);
        
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(2).build());

        logger.info("Found " + similarDocuments.size() + " similar documents.");

        List<String> contents = similarDocuments.stream().map(Document::getText).toList();

        logger.info("Contents of similar documents: " + contents);

        Map<String, Object> map = new HashMap<>();

        map.put("input" , message);
        map.put("documents", String.join("\n", contents));

        PromptTemplate promptTemplate  = new PromptTemplate(faq);

        logger.info("Creating prompt with template: " + promptTemplate);

        Prompt prompt = promptTemplate.create(map);

        logger.info("Sending prompt to chat client.");

        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();

    }

}
