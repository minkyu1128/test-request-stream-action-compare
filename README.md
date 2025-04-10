# AOP 로깅 처리 비교 테스트

요청 Body 의 payload 를 로깅하기 위해 매개변수인 joinPoint 와 Request의 InputStream 사용하는 방법에 대해 차이점을 비교 검증합니다.

## 비교 검증

AOP 에서 요청 body의 payload를 읽는 방법에는 두가지 방법이 존재합니다.

1) ProceedingJoinPoint 사용
2) HttpServletRequest의 InputStream 사용

**ProceedingJoinPoint**     
요청 Body에 대해 ArgumentResolver 에서 처리된 Arguments 데이터 제공

``` java
    @Around("execution(* com.example..*.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[AOP] Payload: " + joinPoint.getArgs()[0].toString());
        .
        .
    }
```

**HttpServletRequest의 InputStream**      
Stream의 특징상 단방향 및 일회성으로 재사용이 불가하며, AOP 호출 전 ProceedingJoinPoint가 Request의 InputStream을 사용하기 때문에 AOP에서 사용하고자 할 경우
Filter에서 RequestWrapping 필요 합니다.

```java

@Around("execution(* com.example..*.controller..*(..))")
public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    System.out.println("[AOP] Payload: " + readBodyAsJsonString(request));
        .
        .
}

private String readBodyAsJsonString(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();    //ServletRequest Wrapper 미사용 시 에러발생 지점 "java.lang.IllegalStateException: getInputStream() has already been called for this request"
    String line;
    while ((line = reader.readLine()) != null) {
        sb.append(line);
    }
    return sb.toString();
}
```

### 그럼 무조건 ProceedingJoin 매개변수를 사용하는게 좋은거 아닌가?

요청 payload 가 AOP 에서만 필요하다면 맞습니다.   
하지만, Intercepter 에서 핸들링이 필요한 경우 Request InputStream 을 사용해야 합니다.

## 테스트

### 1) Request Wrapping 비활성

AOP 가 호출되기 전 ArgumentResolver 에서 Stream 을 사용했기 때문에 Request InputStream 을 읽을 때 **"getInputStream() has already been
called for this request" 에러가 발생** 합니다.

**실행**

```
gradlew :test --tests "com.example.testrequeststreamactioncompare.controller.DisableCachingTest.apiCall"
```

**결과**
![img.png](img.png)

### 2) Request Wrapping 활성

Filter 레벨에서 Request Wrapping 을 통해 복제한 InputStream 데이터를 사용하기 떄문에 Request InputStream 을 읽을 때 오류가 발생하지 않습니다.

**실행**

```
gradlew :test --tests "com.example.testrequeststreamactioncompare.controller.EnableCachingTest.apiCall"
```

**결과**
![img_1.png](img_1.png)

### 3) Request Wrapping 비활성 & Intercepter 활성

Interceptor 에서 Stream 을 사용했기 떄문에 ArgumentResolver 에서 Request InputStream 을 읽으려 할 경우 **"getInputStream() has already been
called for this request" 에러가 발생** 합니다.
**실행**

```
gradlew :test --tests "com.example.testrequeststreamactioncompare.controller.DisableCachingAndEnableInterceptorTest"
```

**결과**
![img_2.png](img_2.png)

### 4) Request Wrapping 활성 & Intercepter 활성

Filter 레벨에서 Request Wrapping 을 통해 복제한 InputStream 데이터를 사용하기 떄문에 Request InputStream 을 읽을 때 오류가 발생하지 않습니다.
**실행**

```
gradlew :test --tests "com.example.testrequeststreamactioncompare.controller.EnableCachingAndEnableInterceptorTest"
```

**결과**
![img_3.png](img_3.png)

### 참고 사이트

* [Spring MVC Workflow](https://pabeba.tistory.com/207)
* [Spring MVC Workflow with ArgumentResolver](https://velog.io/@tmddus2123/spring-FilterInterceptorArgument-Resolver-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)
* [ArgumentResolver의 동작 원리](https://youseong.tistory.com/31)
* [ArgumentResolver와 메시지컨버터](https://velog.io/@uiurihappy/Spring-Argument-Resolver-%EC%A0%81%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9C%A0%EC%A0%80-%EC%A0%95%EB%B3%B4-%EB%B6%88%EB%9F%AC%EC%98%A4%EA%B8%B0)
