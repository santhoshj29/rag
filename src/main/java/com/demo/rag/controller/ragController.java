package com.demo.rag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.rag.service.ragService;

@RestController
@RequestMapping("/olympics")
public class ragController {

    private final ragService ragService;

    @Autowired
    public ragController(ragService ragService) {
        this.ragService = ragService;
    }


    @GetMapping("/faq")
    public ResponseEntity<String> faq(@RequestParam(value = "message", defaultValue = "How Can I Buy Tickets for the Olympics 2024?") String message){
        
        return ResponseEntity.ok(ragService.faq(message));
    }

}
