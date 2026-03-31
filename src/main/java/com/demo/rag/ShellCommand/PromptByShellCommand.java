package com.demo.rag.ShellCommand;

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
import org.springframework.shell.command.annotation.Command;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Command
public class PromptByShellCommand {
    
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Autowired
    public PromptByShellCommand(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Value("classpath:/prompt/spring-boot-reference.st")
    private Resource promptFile;


    public List<String> similarDocumentStrings(String message){
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(2).build());

        List<String> contents = similarDocuments.stream().map(Document::getText).toList();
        return contents;
    }

    @Command(command = "q")
    public String response(@DefaultValue(value = "What is Spring Boot?") String question){
        
        PromptTemplate promptTemplate = new PromptTemplate(promptFile);
        
        Map<String,Object> promptParameters = new HashMap<>();

        promptParameters.put("input", question);
        promptParameters.put("documents", String.join("\n", similarDocumentStrings(question)));


        Prompt prompt = promptTemplate.create(promptParameters);
        
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
        

    }
    
}
