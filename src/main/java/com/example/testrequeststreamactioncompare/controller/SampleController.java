package com.example.testrequeststreamactioncompare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SampleController {

    @PostMapping("/echo")
    public String echo(@RequestParam Map<String, String> payload) throws JsonProcessingException {
        String body = new ObjectMapper().writeValueAsString(payload);
        System.out.println("[Controller] 받은 바디: " + body);
        return "Echo: " + body;
    }
}
