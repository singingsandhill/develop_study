# [Spring Data JPA] Auditing으로 자동 시간 관리하기

JPA를 사용하면서 엔티티에 **생성일시(createdAt)**와 **수정일시(modifiedAt)**가 필요한 경우가 정말 많습니다.
매번 엔티티마다 필드를 선언하고, 직접 시간을 기록하는 코드를 작성하는 것은 번거롭고 비효율적입니다.

Spring Data JPA는 이러한 반복을 줄이기 위해 **Auditing** 기능을 제공하는데,
이 기능을 활용하면 **Entity가 생성·변경되는 시점에 자동으로 시간을 기록**할 수 있습니다.

---

## 1. JPA Auditing이란?

- **Spring Data JPA**가 제공하는 기능으로, 엔티티에 **@CreatedDate**, **@LastModifiedDate** 등과 같은 애노테이션을 붙이면, **엔티티가 생성·수정되는 시점**에 **자동**으로 날짜·시간을 기록해줍니다.
- 이러한 기능 덕분에 매번 `LocalDateTime now = LocalDateTime.now()` 와 같은 코드를 작성할 필요가 없어, 개발 생산성과 코드 간결성을 높일 수 있습니다.

---

## 2. Timestamped 추상 클래스 만들기

엔티티마다 공통적으로 **createdAt**(생성일시)과 **modifiedAt**(수정일시) 같은 필드를 중복 선언하지 않도록 **추상 클래스**를 하나 만들어 두면 편리합니다. 이를 예시로 보겠습니다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;
}
```

### 2.1 `@MappedSuperclass`

- **JPA Entity 클래스**가 **추상 클래스(Timestamped)**를 상속하는 경우,  
  추상 클래스에 선언된 멤버 변수를 **컬럼**으로 인식하도록 만들어주는 애노테이션입니다.
- 즉, 상속받는 엔티티가 `Timestamped` 안에 있는 `createdAt`, `modifiedAt` 컬럼을 사용할 수 있게 됩니다.

### 2.2 `@EntityListeners(AuditingEntityListener.class)`

- **Auditing 기능**을 포함시켜주는 핵심 키워드입니다.
- 스프링이 엔티티의 생성·수정 시점을 **감지**해 필드에 자동으로 시간을 넣어줍니다.

### 2.3 `@CreatedDate`

- 엔티티가 **생성**되어 **저장**될 때의 시간을 자동으로 넣어줍니다.
- `updatable = false`로 설정하여 **최초 생성 시점**만 기록하고, 이후에는 수정되지 않도록 만듭니다.

### 2.4 `@LastModifiedDate`

- 엔티티를 **조회해서 변경**할 때의 시간을 자동으로 갱신해줍니다.
- 예를 들어, 게시글 내용이 수정되면 `modifiedAt`에 **수정된 시점**이 저장됩니다.

### 2.5 `@Temporal`

- 날짜 타입(`java.util.Date`, `java.util.Calendar`)을 매핑할 때 주로 사용합니다.
- 최근에는 `LocalDateTime`과 같은 Java 8 이상의 날짜 타입을 사용할 수 있으므로,  
  관례상으로 적어두지만, Spring Boot에서 `LocalDateTime`을 사용하는 경우 `@Temporal` 없이도 잘 동작하는 환경이 많습니다.
- DB에는 `DATE`, `TIME`, `TIMESTAMP`와 같은 타입들이 있으니 필요에 따라 적절히 매핑 방법을 선택하세요.

---

## 3. `@EnableJpaAuditing` 설정

JPA Auditing 기능을 사용하기 위해서는 **스프링 부트 애플리케이션**이 시작될 때, Auditing을 활성화해야 합니다.  
메인 클래스(예: `SpringBootApplication`)에 다음과 같은 애노테이션을 추가합니다.

```java
@SpringBootApplication
@EnableJpaAuditing
public class MemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }
}
```

- `@EnableJpaAuditing` : JPA Auditing 기능을 **활성화**합니다.

---

## 4. 엔티티에서 상속받아 사용하기

아래처럼 엔티티 클래스가 `Timestamped`를 **상속**만 받으면,  
`createdAt`과 `modifiedAt`을 자동으로 컬럼으로 인식하고, **Auditing** 기능으로 시간까지 관리해줄 수 있습니다.

```java
@Entity
public class Memo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 500)
    private String contents;

    // ...생성자, Getter/Setter 등
}
```

- 이렇게 하면 `Memo`가 생성될 때와 수정될 때, `createdAt`, `modifiedAt` 필드가 **자동으로 기록**됩니다.

---

## 5. 실제 동작 확인해보기

1. 새로운 `Memo` 엔티티를 저장(`em.persist(memo)` 또는 `repository.save(memo)`)하면,
   - JPA가 엔티티의 생성 시점을 감지해 `createdAt`에 현재 시간을 저장합니다.
2. `Memo` 엔티티를 수정 후 다시 저장하면,
   - JPA가 변경 사항을 감지하여 `modifiedAt`에 수정된 시간을 기록해줍니다.
3. DB를 확인해보면, 별도의 시간 입력 로직 없이도 자동으로 날짜·시간 컬럼이 제대로 반영된 것을 확인할 수 있습니다.

---

## 6. 마무리

스프링 데이터 JPA의 **Auditing** 기능은 엔티티가 언제 생성되고 수정되었는지 자동으로 관리해주어,  
**중복된 시간 처리 로직**을 없애고 **코드를 깔끔**하게 만드는 데 아주 유용합니다.

- **Timestamped** 추상 클래스로 구성한 뒤,
- **`@MappedSuperclass`, `@EntityListeners(AuditingEntityListener.class)`, `@CreatedDate`, `@LastModifiedDate`** 등을 활용하면 됩니다.
- 마지막으로 **`@EnableJpaAuditing`**을 꼭 추가해야 Auditing이 활성화된다는 점도 기억하세요.

이처럼 작은 부분이지만, 개발 생산성과 유지보수에 큰 도움이 됩니다. 프로젝트 전체적으로 적용해보면서 더 편리한 개발 경험을 누려보시길 바랍니다!

---

### 참고 자료

- [Spring Boot Reference Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Auditing](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.auditing)
- [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/)
