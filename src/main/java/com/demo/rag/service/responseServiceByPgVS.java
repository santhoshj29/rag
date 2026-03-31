package com.demo.rag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;



@Service
public class responseServiceByPgVS {
    
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Autowired
    public responseServiceByPgVS(ChatClient chatClient, VectorStore vectorStore){
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Value("classpath:/prompt/spring-boot-reference.st")
    private Resource promptFile;

    public List<String> similatDocumentStrings(String message){
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        List<String> contents = similarDocuments.stream().map(Document::getText).toList();
        return contents;
    }

    public String response(String question){

        PromptTemplate promptTemplate = new PromptTemplate(promptFile);

        Map<String, Object> promptParameters = new HashMap<>();

        promptParameters.put("input", question);
        promptParameters.put("documents", String.join("\n", similatDocumentStrings(question)));
        Prompt prompt = promptTemplate.create(promptParameters);
        
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();

        

    }

}
