# Spring IoC와 DI 제대로 이해하기

Spring 프레임워크의 핵심 개념 중 하나인 **IoC(Inversion of Control)**와 **DI(Dependency Injection)**.
처음 Spring을 접하면 “도대체 누가, 어디서, 언제 객체를 생성하고 주입해주는 거지?”라는 의문이 들 수밖에 없습니다.
하지만 이 과정을 이해하면 확실히 Spring 개발이 편해지고, 유지보수에도 큰 도움이 됩니다.

---

## 1. 제어의 역전(Inversion of Control, IoC)

### 1.1 IoC란?

- **IoC**는 말 그대로 **제어의 흐름이 역전**되는 것을 의미합니다.
- 전통적인 애플리케이션 구조에서는 **Controller → Service → Repository** 순으로 객체를 직접 생성하고 의존성을 연결했습니다. 즉, 개발자가 언제 어떤 객체를 생성하고 사용할지 **직접 제어**했죠.
- 하지만 **IoC**를 적용하면 **Repository → Service → Controller** 순으로 의존성이 **자동으로 주입**됩니다. 개발자가 객체를 생성하는 과정을 직접 관리하지 않아도 된다는 점이 핵심입니다.

### 1.2 예시: 메모장 프로젝트

- **강한 결합(tight coupling)** 상태의 메모장 프로젝트를 생각해 봅시다.
- Controller, Service, Repository 간에 직접 생성자를 호출하고 의존성을 주입해주는 구조는 유지보수가 어렵고, 코드도 복잡해집니다.
- **DI(의존성 주입)**를 도입하면, 필요로 하는 객체를 미리 만들어두고 필요한 곳에 주입함으로써 코드가 효율적이고 단순해집니다.

> “외부에서 미리 만든 객체를 주입하는 것이 DI 패턴이라고 했는데,  
> 그렇다면 MemoRepository 같은 객체들은 언제, 어디서, 누가 만들어서 주입해주는 거지?”

바로 이 의문을 해결해주는 핵심이 **Spring의 IoC Container**입니다.

---

## 2. IoC Container와 Bean

### 2.1 Spring IoC 컨테이너

- Spring은 필요한 객체(Bean)를 대신 생성하고, 관리까지 해주는 **IoC 컨테이너**를 제공합니다.
- **Bean**은 말 그대로 Spring IoC 컨테이너에 의해 생성되고, 관리되는 객체를 의미합니다.
- 쉽게 말해, **“개발자가 직접 객체를 생성하지 않고, Spring 컨테이너가 대신 객체를 만들어서 가지고 있다가, 필요할 때 꺼내 쓰게 해주는 구조”**가 됩니다.

### 2.2 Bean 등록 방법

#### 2.2.1 `@Component`

- 클래스 위에 `@Component`를 붙이면, Spring 서버가 뜰 때 해당 클래스를 **Bean**으로 등록해줍니다.
- 예시 코드:

  ```java
  @Component
  public class MemoService {
      // ...
  }
  ```

- `@Component`가 달려 있는 클래스를 **Spring IoC 컨테이너**에 등록해두고, 필요할 때마다 사용할 수 있게 됩니다.
- 등록된 Bean의 이름은 클래스명에서 첫 글자를 소문자로 바꾼 형태가 기본으로 사용됩니다.  
  예) `public class MemoService` → `memoService`

#### 2.2.2 `@ComponentScan`

- Spring 애플리케이션이 구동될 때, `@ComponentScan`이 설정된 패키지와 그 하위 패키지를 스캔하면서, `@Component`가 붙은 클래스를 모두 Bean으로 등록합니다.
- 보통 `@SpringBootApplication`이 선언된 메인 클래스(예: `com.sparta.memo/MemoApplication.java`)가 위치한 패키지를 기본으로 스캔합니다.
  ```java
  @Configuration
  @ComponentScan(basePackages = "com.sparta.memo")
  class BeanConfig {
      // ...
  }
  ```
- Spring Boot에서는 별도의 설정이 없으면, 메인 클래스가 속한 패키지와 그 하위 패키지를 자동으로 스캔합니다.

---

## 3. Spring Bean 사용 방법

### 3.1 `@Autowired`

- **DI(의존성 주입)**를 위한 가장 대표적인 방법입니다.
- **필드 주입** 예시:

  ```java
  @Component
  public class MemoService {

      @Autowired
      private MemoRepository memoRepository;

      // ...
  }
  ```

  - Spring IoC 컨테이너에서 관리되고 있는 `memoRepository` Bean 객체가 **필드**에 자동으로 주입됩니다.

- **생성자 주입** 예시:

  ```java
  @Component
  public class MemoService {

      private final MemoRepository memoRepository;

      @Autowired
      public MemoService(MemoRepository memoRepository) {
          this.memoRepository = memoRepository;
      }

      // ...
  }
  ```

  - **생성자 주입**은 객체 불변성을 확보할 수 있어 유지보수성이 좋아서 권장되는 방식입니다.
  - 또한, **Spring 4.3** 버전부터는 **생성자가 하나**만 존재하면 `@Autowired`를 생략할 수도 있습니다.

#### 3.1.1 Lombok과 생성자 주입

- **Lombok**의 `@RequiredArgsConstructor`를 사용하면 코드가 더욱 간결해집니다.
  ```java
  @Component
  @RequiredArgsConstructor
  public class MemoService {
      private final MemoRepository memoRepository;
      // ...
  }
  ```
  - `final`로 선언된 멤버 변수를 파라미터로 하는 생성자를 자동 생성해줍니다.

### 3.2 `ApplicationContext`로 Bean 가져오기

- 간혹 Bean을 **수동**으로 가져와야 할 상황이 있을 수 있습니다. 이 때는 `ApplicationContext`를 통해 Bean을 조회할 수 있습니다.

  ```java
  @Component
  public class MemoService {
      private final MemoRepository memoRepository;

      public MemoService(ApplicationContext context) {
          // 1. Bean 이름으로 가져오기
          MemoRepository memoRepository = (MemoRepository) context.getBean("memoRepository");

          // 2. Bean 클래스 형식으로 가져오기
          // MemoRepository memoRepository = context.getBean(MemoRepository.class);

          this.memoRepository = memoRepository;
      }

      // ...
  }
  ```

- 하지만 일반적인 Spring 애플리케이션에서는 `@Autowired` 또는 생성자 주입 방식을 사용하기 때문에, `ApplicationContext`를 직접 호출하는 경우는 드뭅니다.

---

## 4. Spring 3 Layer Annotation

Spring 애플리케이션을 구성할 때, 보통 **Controller**, **Service**, **Repository** 3가지 계층을 많이 사용합니다. 각각의 역할을 명확히 분리하기 위해, Spring은 아래와 같은 3 Layer 전용 애노테이션을 제공합니다.

1. **@Controller**, **@RestController**
   - 웹 요청을 받고, 응답을 반환하는 **컨트롤러** 클래스
2. **@Service**
   - 비즈니스 로직을 담당하는 **서비스** 클래스
3. **@Repository**
   - DB와의 소통을 담당하는 **저장소** 클래스

이 애노테이션들은 모두 내부적으로 `@Component`를 포함하고 있어, 별도의 `@Component` 없이도 Bean으로 등록됩니다. 이를 통해 “이 클래스가 어떤 역할인지”를 좀 더 명시적으로 보여줄 수 있습니다.

---

## 5. 마무리 및 추가 팁

- **제어의 역전(IoC)** 개념을 잘 이해하면, “도대체 누가, 어디서 객체를 만들어서 넣어주는 거지?” 같은 의문이 말끔히 해결됩니다.
- **DI(의존성 주입)**를 통해 느슨한 결합(Loosely Coupled) 구조를 만들면, 유지보수나 확장에 유리한 구조를 갖출 수 있습니다.
- **Spring IoC 컨테이너**가 **Bean**을 자동으로 생성하고 관리해주기 때문에, 개발자는 **비즈니스 로직**에 좀 더 집중할 수 있습니다.
- 3 Layer 구조(Controller, Service, Repository)는 역할 분리를 명확히 하고, 확장성 높은 애플리케이션 설계에 유리합니다.
- 필요하다면 Bean의 **Scope(범위)**를 설정(`@Scope("prototype")` 등)할 수도 있지만, Spring Boot의 대부분의 기본 설정에서는 **Singleton**으로 활용하는 경우가 많습니다.

> **다음 강의 예고**  
> 이제 IoC 컨테이너와 Bean 개념을 이해했으니, Spring 애플리케이션에서 생성되는 **각 Bean의 라이프사이클**과 **어떤 시점에서 어떤 Bean들이 관리되는지** 자세히 파헤쳐보면 더 큰 그림을 볼 수 있습니다. 다음 강의에서 함께 알아보겠습니다!

---

**정리**  
IoC(제어의 역전)과 DI(의존성 주입)는 Spring 프레임워크의 핵심 개념입니다.
Spring이 제공하는 IoC 컨테이너는 Bean을 자동으로 생성, 관리하여 개발자가 객체 생성과 주입 과정을 직접 제어하지 않아도 되게끔 해 줍니다.
이를 통해 애플리케이션 구조를 깔끔하고 확장성 있게 구성할 수 있습니다.
