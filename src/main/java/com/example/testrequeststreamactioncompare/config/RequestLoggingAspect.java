package com.example.testrequeststreamactioncompare.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class RequestLoggingAspect {
    @Around("execution(* com.example..*.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 🚨 원본 request에서 body 완전히 소비
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        System.out.println("[AOP] Body: " + body);


        System.out.println("[AOP] Payload: " + body);  // DB 저장 대신 출력

        Object result = joinPoint.proceed();

        System.out.println("[AOP] Response 처리 완료 - " + result);

        return result;
    }


}
