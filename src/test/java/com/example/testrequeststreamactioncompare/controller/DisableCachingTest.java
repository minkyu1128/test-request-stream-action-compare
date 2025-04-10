package com.example.testrequeststreamactioncompare.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DisableCachingTest {
    @LocalServerPort
    int port;
    @Autowired
    ObjectMapper objectMapper;
    RestTemplate restTemplate = new RestTemplate();

    @Test
    void apiCall() throws Exception {
        // given
        Map<String, String> body = Map.of(
                "name", "mk.park",
                "gender", "male"
        );
        String json = objectMapper.writeValueAsString(body);

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        String url = "http://localhost:" + port + "/api/echo";
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(json);
    }
}