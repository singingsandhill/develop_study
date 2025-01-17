# [TIL] 영속성 컨텍스트와 JPA 트랜잭션 정리

> JPA의 가장 핵심적인 개념인 **영속성 컨텍스트(Persistence Context)**와 이를 실제 운영환경에서 어떻게 다루는지.
> 영속성 컨텍스트가 어떻게 동작하는지, 그리고 트랜잭션과 함께 어떤 식으로 엔티티를 관리하는지 정리.

---

## 1. 영속성 컨텍스트(Persistence Context)란?

- **Persistence**를 한글로 번역하면 “영속성, 지속성”이라는 뜻입니다.
- JPA에서 말하는 영속성 컨텍스트는 **“엔티티(Entity) 객체를 효율적으로 쉽게 관리하기 위해 만들어진 공간”**을 의미합니다.
- 개발자는 JPA를 통해 SQL을 직접 작성하지 않고도 DB에 데이터를 저장, 조회, 수정, 삭제할 수 있는데, 이 모든 과정을 영속성 컨텍스트가 중간에서 관리하면서 수행합니다.

### 1.1 EntityManager, EntityManagerFactory

- 엔티티 매니저(EntityManager)는 영속성 컨텍스트에 접근하여 엔티티를 저장, 조회, 수정, 삭제할 수 있는 관리자입니다.
- 엔티티 매니저는 엔티티 매니저 팩토리(EntityManagerFactory)를 통해 생성할 수 있습니다.
- 보통 하나의 DB 당 하나의 `EntityManagerFactory` 객체를 생성하며, 실제 애플리케이션 구동 시점에 딱 한 번 만들어집니다.
- Spring Boot 환경에서는 `EntityManagerFactory`와 `EntityManager`를 **자동**으로 설정해주므로, 직접 팩토리를 만들 일은 많지 않습니다.

---

## 2. 트랜잭션(Transaction)과 JPA

### 2.1 트랜잭션이란?

- DB의 무결성과 정합성을 유지하기 위한 **하나의 논리적 작업 단위**입니다.
- 여러 개의 SQL을 하나로 묶어서 모두 성공하면 **Commit**, 하나라도 실패하면 **Rollback**하는 방식으로 DB를 안전하게 보호합니다.

### 2.2 JPA 트랜잭션의 특징

- **영속성 컨텍스트**에 엔티티를 저장(persist)한다고 해서 곧바로 DB에 반영되는 것이 아닙니다.
- DB의 트랜잭션이 **Commit**될 때까지 변경사항을 모아두었다가, 마지막에 한 번에 실제 쿼리를 날리는 **쓰기 지연(Write-behind) 기법**을 사용합니다.
- JPA에서는 트랜잭션이 반드시 필요한 작업(INSERT, UPDATE, DELETE)을 수행할 때, 내부적으로 **`EntityTransaction`**을 이용해 커밋/롤백 과정을 거칩니다.

---

## 3. 영속성 컨텍스트 내부 동작

### 3.1 1차 캐시 (First-level Cache)

- 영속성 컨텍스트는 내부적으로 Map 형태의 **1차 캐시**를 가지고 있습니다.
- 엔티티의 식별자(@Id) 값을 **key**, 실제 엔티티 객체를 **value**로 저장하여 **DB 조회를 최소화**합니다.
- 한 번 조회한 엔티티는 다음에 같은 식별자로 재조회할 때, **DB를 재조회하지 않고** 1차 캐시에서 꺼내오는 장점이 있습니다.

### 3.2 쓰기 지연 저장소(ActionQueue)

- INSERT나 UPDATE, DELETE 같은 변경 쿼리는 한 번에 DB로 보내지 않고, **쓰기 지연 저장소**에 쌓아둡니다.
- 트랜잭션을 커밋하거나 `flush()`를 호출하는 시점에 **한꺼번에** SQL을 실행합니다.

### 3.3 변경 감지(Dirty Checking)

- JPA는 엔티티를 영속성 컨텍스트에 저장할 때, **최초 상태(loadedState)**를 따로 기록해 둡니다.
- 트랜잭션 종료 시점(커밋 이전, `flush()` 시점)에서 **현재 상태**와 **최초 상태**를 비교해 변경이 있으면 자동으로 **UPDATE 쿼리**를 생성해 DB에 반영합니다.
- 별도의 `em.update(...)` 호출이 없는 이유도 바로 이 **변경 감지** 덕분입니다.

---

## 4. 엔티티의 상태

1. **비영속(Transient)**

   - `new` 키워드로 막 생성된 상태의 엔티티. 아직 영속성 컨텍스트에 등록되지 않아 **JPA가 전혀 관리하지 않는** 상태를 말합니다.

2. **영속(Managed)**

   - `em.persist(entity)` 등을 호출하여 영속성 컨텍스트에 등록된 엔티티. JPA가 관리하고 있으므로 **변경 감지** 등의 혜택을 받습니다.

3. **준영속(Detached)**

   - 원래 영속 상태였던 엔티티가 **영속성 컨텍스트에서 분리**된 상태입니다.
   - `em.detach(entity)` 또는 `em.clear()`, `em.close()`와 같이 영속성 컨텍스트에서 제거되거나 영속성 컨텍스트 자체가 초기화되면, 더 이상 JPA가 해당 엔티티를 관리하지 않습니다.
   - 이 상태에서는 변경 감지가 일어나지 않습니다.

4. **삭제(Removed)**
   - `em.remove(entity)`를 통해 삭제 요청된 엔티티. 트랜잭션 커밋 시점에 **DELETE 쿼리**가 DB로 날아갑니다.

### 4.1 준영속 상태에서 다시 영속으로: `merge()`

- 준영속 혹은 비영속 엔티티를 `em.merge(entity)` 하면 식별자(@Id) 값을 기준으로 영속성 컨텍스트를 조회합니다.
  - 이미 엔티티가 있으면 **UPDATE**,
  - 없다면 **새로 INSERT**해서 영속 상태로 만듭니다.
- 주의할 점은, **파라미터로 받은 준영속 엔티티 자체가 영속 상태로 바뀌는 것이 아니라**, 내부적으로 **새로운 엔티티**를 만들고 병합한 뒤 이를 영속상태로 만들어 반환합니다.

---

## 5. Spring Boot와 JPA

### 5.1 `application.properties` 설정

```properties
# Hibernate 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
```

- `ddl-auto`는 테이블 생성·삭제·검증 전략을 지정합니다.
- `show_sql`, `format_sql`, `use_sql_comments` 옵션을 통해 콘솔에서 보기 편한 SQL 로그를 확인할 수 있습니다.

### 5.2 Spring의 트랜잭션 관리

- Spring은 `@Transactional` 애노테이션으로 트랜잭션을 시작·종료할 수 있게 해줍니다.
- **readOnly** 옵션을 통해 읽기 전용 트랜잭션을 선언할 수도 있습니다.
- 부모 메서드와 자식 메서드 간에 **트랜잭션 전파(Propagation)**를 통해 트랜잭션을 공유하거나 각각 독립적으로 실행할 수 있습니다. 기본값은 `REQUIRED`이며, 부모 메서드가 이미 트랜잭션을 갖고 있으면 자식 메서드는 해당 트랜잭션에 합류합니다.

---

## 6. 마무리

- **영속성 컨텍스트**는 엔티티 객체를 모아 두고 관리하면서, 1차 캐시·쓰기 지연 저장소·변경 감지 등 효율적인 메커니즘을 제공합니다.
- 이와 더불어 **트랜잭션**이 함께 동작해야만 INSERT·UPDATE·DELETE 쿼리가 실제 DB에 반영되므로, JPA 코드를 작성할 때는 항상 **트랜잭션 환경**을 적절히 구성해야 합니다.
- Spring Boot 환경에서는 대부분의 설정이 자동화되어 있어, `@Transactional`과 `Repository` 계층만 잘 사용해도 JPA를 손쉽게 활용할 수 있습니다.
- 오늘 배운 내용을 통해, “**엔티티는 언제 DB에 반영되고, 어떻게 업데이트되는지**”에 대한 궁금증을 어느 정도 해소할 수 있었습니다.
- 이후에는 스프링 데이터 JPA와 연계해 **더 간단하게** CRUD를 구현하고, 복잡한 쿼리도 필요한 경우에만 작성하는 방식을 살펴보면 좋겠습니다.

> **정리하자면**, 영속성 컨텍스트의 이해 없이 JPA를 사용하는 것은 어렵습니다. 트랜잭션 관리와 함께 영속성 컨텍스트가 어떻게 엔티티를 다루는지 확실히 이해해두면, 유지보수와 성능 최적화 모두에 큰 도움이 될 것입니다.

---

**Reference**

- SpringBoot Docs: [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- Hibernate ORM Docs: [Hibernate Official Documentation](https://hibernate.org/orm/documentation/)
- JPA Specification: [Jakarta Persistence](https://jakarta.ee/specifications/persistence/)
