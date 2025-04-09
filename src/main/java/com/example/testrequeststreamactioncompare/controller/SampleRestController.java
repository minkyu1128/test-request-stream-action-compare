package com.example.testrequeststreamactioncompare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SampleRestController {
    @PostMapping("/echo")
    public String echo(@RequestBody Map<String, String> payload) throws JsonProcessingException {
        String body = new ObjectMapper().writeValueAsString(payload);
        System.out.println("[Controller] 받은 바디: " + body);
        return "Echo: " + body;
    }
}
