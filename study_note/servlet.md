# 1. 서블릿 기본

## 1.1 서블릿이란?

서블릿은 동적으로 웹 페이지를 생성하도록 설계된 서버측 Java 프로그램. <br>
일반적인 서블릿 작업 흐름에서:

- 클라이언트(일반적으로 브라우저)는 서버에 HTTP 요청을 보냄.
- 서블릿 컨테이너는 HttpServletRequest 및 HttpServletResponse 객체를 생성.
- 컨테이너는 요청을 처리해야 하는 서블릿을 결정.
- 올바른 서블릿의 service() 메소드가 호출되고, HTTP 메소드에 따라 doGet() 또는 doPost()를 호출.
- 서블릿은 요청을 처리하여 동적 페이지나 JSON 응답을 생성하고 결과를 다시 'HttpServletResponse'에 사용.
- 컨테이너는 클라이언트에 응답을 반환하고 생성된 개체를 모두 정리.

## 1.2 서블릿을 수동으로 구현하지 않는 이유?

강력한 애플리케이션의 경우 가능한 모든 요청(예: /api/hello, /user/login 등)을 처리하려면 많은 서블릿이 필요. <br>
Spring이 보다 유연한 접근 방식을 도입한 이유.

# 2. DispatcherServlet 및 Front Controller 패턴

## 2.1 Front Controller 패턴

Front Controller 패턴에서는 단일 컨트롤러(Front Controller)가 모든 요청을 처리하고 조정. <br>
Spring MVC에서는 DispatcherServlet이 이 역할을 수행.

## 2.2 DispatcherServlet의 작동

클라이언트가 HTTP 요청을 보낼 때:

- DispatcherServlet은 먼저 요청을 수신.
- URL, HTTP 메소드 및 기타 요소를 살펴본 다음 핸들러 매핑을 사용하여 어느 컨트롤러 메소드가 이 특정 요청을 처리하는지 파악.
- DispatcherServlet은 일치하는 Controller 메서드를 호출.
- 컨트롤러는 모델을 사용하여 모든 비즈니스 논리 또는 데이터 요구 사항을 처리한 다음 처리된 데이터("모델")와 이를 렌더링해야 하는 뷰를 모두 반환.
- DispatcherServlet은 올바른 보기(예: JSP 파일, Thymeleaf 템플릿 또는 JSON 응답)를 선택하는 ViewResolver에 데이터를 전달.
- 마지막으로 DispatcherServlet은 렌더링된 뷰를 HTTP 응답으로 클라이언트에 다시 보냄.
- 결과: 컨트롤러 메서드(예: hello(), login(), signup())만 작성하고 Spring이 라우팅, 요청 매핑 및 응답 렌더링과 같은 지루한 부분을 처리.

# 3. 예: Spring MVC의 요청 매핑

## 3.1 @RestController가 포함된 컨트롤러

```java
    @RestControllerpublic
    class HelloController {

        @GetMapping("/api/hello")
        public String hello() {
            return "Hello World!";
        }
    }
```

- @RestController: 이 클래스를 HTTP 응답 본문(주로 JSON이지만 위와 같이 일반 텍스트일 수 있음)에서 직접 데이터를 반환하는 컨트롤러로 표시.
- @GetMapping("/api/hello"): 애플리케이션이 /api/hello에 대한 GET 요청을 수신할 때마다 이 메서드를 호출하도록 Spring MVC에 지시.

# 4. MVC 적용

    1. 사용자 작업: 사용자가 https://yourdomain.com/api/hello를 방문.
    2. Front Controller: DispatcherServlet은 요청을 분석하고 핸들러 매핑을 참조.
    3. 컨트롤러: HelloController.hello() 메서드가 호출.
    4. 모델 및 뷰: 이 메서드는 문자열(또는 JSON 또는 다른 형식)을 반환.
    5. 응답: DispatcherServlet은 응답을 사용자에게 다시 보냄.

# 5. 스프링 MVC의 장점

    일관된 아키텍처: 단일 지점(DispatcherServlet)이 모든 요청을 처리하여 복잡성을 줄임.
    유연한 보기 기술: JSP, Thymeleaf, Mustache를 혼합하여 사용하거나 JSON을 반환(@RestController 사용).
    강력한 구성: 주석 기반 접근 방식은 XML이나 상용구가 적다는 것을 의미.
    다른 Spring 기능과의 통합: Spring 생태계의 일부이므로 Spring Security, Spring Data 등과 쉽게 통합.
