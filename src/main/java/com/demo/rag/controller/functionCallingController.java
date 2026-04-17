package com.demo.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.demo.rag.service.functionCalling;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/call")
public class functionCallingController {

    private final functionCalling functionCalling;
    private final ChatClient chatClient;

    @Autowired
    public functionCallingController(functionCalling functionCalling, ChatClient chatClient) {
        this.functionCalling = functionCalling;
        this.chatClient = chatClient;
    }

    @GetMapping("/message")
    public ResponseEntity<String> response(@RequestParam(value = "message", defaultValue = "Which is the biggest city in California?") String message){
        String response = functionCalling.response(message);
        return ResponseEntity.ok(response);

    }


    @GetMapping("/city")
    public ResponseEntity<String> CurrentWeatherOfCity(@RequestParam(value = "city", defaultValue = "San Francisco") String city){

        SystemMessage systemMessage = new SystemMessage("You're a helpful AI Assistant answering questions about cities around the world");
        
        UserMessage userMessage = new UserMessage("What is the current weather in " + city + "?");

        List<Message> ListOfMessage = List.of(systemMessage, userMessage);

        OpenAiChatOptions options = OpenAiChatOptions.builder().toolNames(Set.of("currentWeatherFunction")).build();

        Prompt prompt = new Prompt(ListOfMessage, options);

        
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();

        return ResponseEntity.ok(chatResponse.getResult().getOutput().getText());
        

    }

}
