package com.demo.rag.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class functionCalling {
    
    private final ChatClient chatClient;

    @Autowired
    public functionCalling(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String response(String message){

        SystemMessage systemMessage = new SystemMessage("You're a helpful AI Assistant answering questions about cities around the world");
        
        UserMessage userMessage = new UserMessage(message);

        List<Message> ListOfMessages = List.of(systemMessage, userMessage);

        Prompt prompt = new Prompt(ListOfMessages);

        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();

        return chatResponse.getResult().getOutput().getText();

    }

}
