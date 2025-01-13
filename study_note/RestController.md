---
## 1. 들어가며
Spring Boot에서 RESTful API를 개발할 때, 클라이언트가 보내는 데이터를 서버에서 어떻게 받을 수 있을까요?
Spring에서는 다양한 어노테이션을 통해 이를 편리하게 처리할 수 있도록 지원합니다.

- `@PathVariable`
- `@RequestParam`
- `@ModelAttribute`
- `@RequestBody`
- `@RequestParam`, `@RequestBody`가 생략 가능한 경우와 그 동작 방식
---

## 2. @PathVariable

`@PathVariable`은 **URL 경로(Path) 내의 변수**를 바인딩할 때 사용합니다.

### 2.1 사용 예시

예를 들어, `/users/{id}` 형태의 URL에서 `{id}` 값을 메서드 파라미터로 받고 싶다면 다음과 같이 작성할 수 있습니다.

```java
@RestController
@RequestMapping("/users")
public class UserController {

    // GET /users/1 과 같은 요청을 받을 때
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id) {
        return "User id is: " + id;
    }
}
```

- `@GetMapping("/{id}")`에서 `{id}`는 경로 변수입니다.
- `@PathVariable Long id`는 URL에 포함된 {id} 값을 Long 타입으로 바인딩합니다.

### 2.2 주의 사항

- `@PathVariable`에 별도의 변수 이름을 지정하지 않으면, 메서드 파라미터 이름과 `{}` 안의 경로 변수가 동일해야 합니다.
- 만약 `{userId}` 형태로 경로 변수를 사용하고 싶다면 `@PathVariable("userId")`처럼 지정하여 사용할 수 있습니다.

---

## 3. @RequestParam

`@RequestParam`은 **쿼리 파라미터(Query Parameter)** 나 **폼(Form) 데이터** 등을 매핑받을 때 사용합니다. 예: `?name=aaa&email=bbb`

### 3.1 사용 예시

아래 코드는 `/users` 경로에서 `GET /users?name=alice&email=alice@example.com` 같은 요청을 받을 때 `name`과 `email` 값을 파라미터로 매핑합니다.

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String getUser(@RequestParam String name,
                          @RequestParam String email) {
        return "Name: " + name + ", Email: " + email;
    }
}
```

### 3.2 옵션 파라미터

필수 파라미터가 아닌 경우에는 `required = false` 옵션을 주어 파라미터가 없어도 동작하도록 할 수 있습니다.

```java
@GetMapping("/search")
public String searchUser(@RequestParam(required = false) String name) {
    if (name == null) {
        return "No name parameter";
    }
    return "Search user with name: " + name;
}
```

### 3.3 기본값 설정

파라미터가 넘어오지 않았을 때 기본값을 설정하고 싶다면 `defaultValue`를 활용합니다.

```java
@GetMapping("/page")
public String paging(@RequestParam(defaultValue = "0") int page) {
    return "Now page: " + page;
}
```

### 3.4 @RequestParam 생략 가능 여부

실제로 **간단한(단일) 파라미터**일 때는 `@RequestParam`을 생략해도, 스프링이 **이름이 일치하는 쿼리 파라미터**를 찾아서 바인딩합니다. 예를 들어 아래와 같이 작성해도 동작합니다.

```java
@GetMapping("/omit")
public String omitRequestParam(String name) {
    return "Name: " + name;
}
```

- 다만, 이를 사용하는 것은 강력히 권장되지 않습니다.
- 이유: 스프링이 내부적으로 `@ModelAttribute` 로 동작할지, 혹은 다른 방식으로 동작할지 헷갈릴 수 있으며, 추가 파라미터가 생기거나 복잡해지면 동작이 명확하지 않아질 수 있습니다.
- 기본 변수일경우에 @RequestParam, 객체같은 경우에는 @ModelAttribute로 동작.
- 따라서 코드 가독성과 유지보수성을 위해 **명시적으로 `@RequestParam`을 사용하는 것이 좋습니다**.

---

## 4. @ModelAttribute

`@ModelAttribute`는 **폼 태그로 전송되는 데이터**나 **쿼리 파라미터를 한 번에 객체로 매핑**할 때 사용합니다. 흔히 DTO(Data Transfer Object)나 Form 객체와 연동될 때 편리합니다.

### 4.1 사용 예시

예를 들어, `UserForm`이라는 DTO 클래스가 있고, 해당 DTO에 `name`, `email` 필드를 넣어두었다고 가정해봅시다.

```java
public class UserForm {
    private String name;
    private String email;

    // 게터 & 세터
}
```

`@ModelAttribute`를 사용하면 다음과 같이 여러 파라미터를 객체에 바인딩할 수 있습니다.

```java
@PostMapping("/create")
public String createUser(@ModelAttribute UserForm userForm) {
    return "User Created! Name: " + userForm.getName() + ", Email: " + userForm.getEmail();
}
```

- 스프링은 요청 파라미터(쿼리 파라미터, 폼 데이터 등)의 `name`, `email` 값을 `UserForm` 객체의 필드에 자동으로 매핑합니다.
- HTML Form으로 전송되는 데이터도 같은 방식으로 바인딩됩니다.

### 4.2 Model에 담기는 객체

`@ModelAttribute`가 붙은 객체는 View 단에서 사용하기 위해 자동으로 Model에도 추가됩니다. REST API 개발 시에는 뷰를 사용하지 않는 경우가 많아 체감이 덜할 수 있지만, 전통적인 MVC 형태라면 뷰 렌더링 시 유용하게 사용할 수 있습니다.

---

## 5. @RequestBody

`@RequestBody`는 **HTTP 요청 바디에 포함된 JSON이나 XML 등의 데이터를 객체로 변환**할 때 사용합니다. RESTful API에서 가장 많이 쓰이는 방식 중 하나입니다.

### 5.1 사용 예시

JSON 형태로 들어오는 요청을 객체로 매핑해야 한다면 `@RequestBody`를 사용할 수 있습니다.

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserForm userForm) {
        // userForm에는 JSON 형식으로 받은 name, email 값이 매핑됩니다.
        return ResponseEntity.ok("User created: " + userForm.getName());
    }
}
```

- 클라이언트에서 다음과 같은 JSON을 POST로 전송한다고 가정합니다.
  ```json
  {
    "name": "alice",
    "email": "alice@example.com"
  }
  ```
- `@RequestBody UserForm userForm`은 위 JSON 내용을 `UserForm` 객체에 매핑합니다.
- 컨트롤러 메서드 안에서 `userForm.getName()` 등으로 값을 사용할 수 있습니다.

### 5.2 @RequestBody 생략 가능 여부

기본적으로 **객체 타입의 파라미터**를 받을 때 **`@RequestBody`**를 명시하지 않으면, 스프링은 이를 `@ModelAttribute`로 해석하려고 시도합니다.

- 즉, 쿼리 파라미터 혹은 폼 데이터 방식으로 들어온 데이터를 바인딩하려고 할 것입니다.
- 하지만 실제로는 우리가 **JSON RequestBody**를 받고 싶은 것이라면, `@RequestBody`를 생략하면 제대로 매핑되지 않습니다.
- 스프링이 객체를 생성해서 필드를 채우긴 하겠지만, **HTTP Body 내용**이 아닌 **쿼리 파라미터**나 **폼 데이터** 위주로 바인딩을 시도하므로 JSON 데이터는 무시될 가능성이 큽니다.

결론적으로, **JSON이나 XML 등 바디로 데이터를 전송**하는 **RESTful API 방식**을 사용한다면 `@RequestBody`를 **반드시 명시**해야 합니다.

- 단, `@RequestBody`가 생략 가능해 보이는 일부 예외적인 경우(예: 새롭게 도입된 HTTP Interface 기능 등)가 있지만,
- 일반적인 `@RestController` + `Controller` 방식에서는 `@RequestBody`가 없다면 스프링이 바디 파싱(Deserialization)을 진행하지 않습니다.

---

## 6. 요약 및 마무리

1. **@PathVariable**
   - URL 경로 값으로부터 파라미터를 받아오는 데 사용
   - ex) `/users/{id}` → `@PathVariable Long id`
2. **@RequestParam**
   - 쿼리 파라미터나 폼(Form) 데이터를 단일(혹은 여러) 변수로 받아오는 데 사용
   - 간단한 파라미터의 경우 이름만 일치하면 생략해도 동작하지만, 명시적으로 사용 권장
3. **@ModelAttribute**
   - 여러 파라미터를 한 번에 객체로 묶어 받아오는 데 사용 (특히 HTML Form을 통한 전송 시 편리)
   - 전통적인 MVC에서는 뷰 렌더링용 Model에 자동 등록
4. **@RequestBody**
   - HTTP Body에 포함된 JSON/XML 데이터를 DTO 객체로 매핑할 때 사용
   - 생략 시, JSON이 아닌 폼 데이터로 간주될 수 있어 정상적인 바인딩이 되지 않을 가능성이 큼 → **반드시 명시**

**정리하자면**

- 경로에 값이 들어 있는 경우엔 `@PathVariable`
- 쿼리 파라미터로 전달되는 경우엔 `@RequestParam` (가급적 명시)
- 여러 파라미터를 한 번에 객체로 묶어서 가져오려면 `@ModelAttribute`
- JSON 형태의 Body를 객체로 매핑하려면 `@RequestBody`

각 어노테이션의 특성과 사용 용도를 정확히 이해하시면, 훨씬 깔끔하고 유지보수하기 쉬운 코드를 작성할 수 있습니다.

---
