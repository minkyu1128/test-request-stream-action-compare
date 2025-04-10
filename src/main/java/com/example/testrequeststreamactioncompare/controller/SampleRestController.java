package com.example.testrequeststreamactioncompare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SampleRestController {
    @PostMapping("/echo")
    public String echo(@RequestBody Map<String, String> payload, HttpServletRequest request) throws IOException {
        String body = new ObjectMapper().writeValueAsString(payload);
//        readBodyAsJsonString(request);
        System.out.println("[Controller] 받은 바디: " + body);
        return body;
    }
}
