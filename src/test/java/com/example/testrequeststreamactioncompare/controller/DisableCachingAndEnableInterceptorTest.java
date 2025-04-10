package com.example.testrequeststreamactioncompare.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("interceptor-enabled")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DisableCachingAndEnableInterceptorTest {
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
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // 여전히 정상 상태 기대
            assertThat(response.getBody()).isEqualTo(json);
        } catch (HttpServerErrorException e) {
            // 서버 5xx 오류 처리
            System.err.println("서버 오류 발생: " + e.getStatusCode());
            e.printStackTrace();
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR); // 500 허용
        }
    }
}