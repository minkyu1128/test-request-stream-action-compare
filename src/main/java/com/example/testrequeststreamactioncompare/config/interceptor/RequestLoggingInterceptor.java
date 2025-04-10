package com.example.testrequeststreamactioncompare.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.testrequeststreamactioncompare.config.aop.RequestLoggingAspect.readBodyAsJsonString;


@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("-----------------------------------------------------------------");
        // 래퍼 클래스에서 body 캐싱
        String payload = readBodyAsJsonString(request);
        System.out.println("[Interceptor] Request Payload: " + payload);
        return true;
    }

}
