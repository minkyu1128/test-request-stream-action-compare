package com.example.testrequeststreamactioncompare.config.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;

@Aspect
@Component
public class RequestLoggingAspect {
    @Around("execution(* com.example..*.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("-----------------------------------------------------------------");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        //전처리
        System.out.println("[AOP] Payload(from joinPoint): " + joinPoint.getArgs()[0].toString());
        String jsonBody = readBodyAsJsonString(request);
        System.out.println("[AOP] Payload(from InputStream): " + jsonBody);

        //프로세스 호출
        Object result = joinPoint.proceed();

        //후처리
        System.out.println("[AOP] Response 처리 완료 - " + result);

        System.out.println("-----------------------------------------------------------------");
        return result;
    }

    public static String readBodyAsJsonString(HttpServletRequest request) throws IOException {
        String result = "";
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();    //ServletRequest Wrapper 미사용 시 에러발생 지점 "java.lang.IllegalStateException: getInputStream() has already been called for this request"
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            return result;
        }
    }

}
