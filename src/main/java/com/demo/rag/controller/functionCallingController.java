package com.demo.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.demo.rag.service.functionCalling;

@RestController
@RequestMapping("/call")
public class functionCallingController {

    private final functionCalling functionCalling;

    @Autowired
    public functionCallingController(functionCalling functionCalling) {
        this.functionCalling = functionCalling;
    }

    @GetMapping("/message")
    public ResponseEntity<String> response(@RequestParam(value = "message", defaultValue = "Which is the biggest city in California?") String message){
        String response = functionCalling.response(message);
        return ResponseEntity.ok(response);

    }

}
