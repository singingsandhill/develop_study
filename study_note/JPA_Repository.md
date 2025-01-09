# JPA의 EntityManager와 JpaRepository 비교: 데이터 접근 계층 설계 방법

JPA(Java Persistence API)를 사용할 때 데이터 접근 계층을 설계하는 방법은 다양합니다.
그중 가장 흔히 사용되는 방식은 EntityManager를 직접 사용하는 방법과 Spring Data JPA에서 제공하는 JpaRepository를 활용하는 것입니다.
이 두 방식은 각각 장단점이 뚜렷하기 때문에 프로젝트의 요구사항에 따라 적절한 방법을 선택하는 것이 중요합니다.

# 1. EntityManager를 사용하는 방식

```java
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
```

## 특징

1. JPA의 표준 API 사용
   - @PersistenceContext를 통해 EntityManager를 주입받아 사용합니다.
   - JPA의 표준 메서드(persist, find, merge, remove 등)를 호출하여 데이터베이스 작업을 수행합니다.
2. 직접 구현 필요
   - CRUD(Create, Read, Update, Delete)와 같은 기본적인 데이터 접근 로직을 개발자가 직접 구현해야 합니다.
   - 위 예제에서는 save와 find 메서드를 작성했으며, 추가적인 기능이 필요할 경우 메서드를 계속 추가해야 합니다.
3. 높은 유연성
   - 데이터 접근 로직을 세밀하게 제어할 수 있습니다.
   - JPQL(Java Persistence Query Language) 또는 Native Query를 직접 작성하여 복잡한 쿼리를 구현할 수 있습니다.

## 장점

- 유연성: 복잡한 데이터 로직을 커스터마이징할 수 있습니다.
- 표준 JPA: Spring에 종속적이지 않은 표준 JPA 구현으로, 다른 환경에서도 동일한 코드를 사용할 수 있습니다.

## 단점

- 생산성 저하: CRUD 메서드를 매번 구현해야 하므로 개발 시간이 더 걸립니다.
- 러닝 커브: JPA에 대한 깊은 이해가 필요합니다.

# 2. Spring Data JPA의 JpaRepository 사용

```java
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

## 특징

1. Spring Data JPA 활용

   - JpaRepository는 Spring Data JPA에서 제공하는 인터페이스로, 이를 상속받으면 기본 CRUD 메서드가 자동으로 구현됩니다.
   - save, findById, delete, findAll 등 데이터 접근에 필요한 주요 메서드가 포함되어 있습니다.

2. 추가 메서드 정의

- 메서드 이름을 기반으로 자동으로 쿼리를 생성할 수 있습니다.
  - 예: findByName(String name) 메서드를 추가하면 name을 조건으로 하는 쿼리가 자동 생성됩니다.
- JPQL이나 Native Query가 필요한 경우 @Query를 사용하여 직접 정의할 수도 있습니다.

3. 생산성 향상

- 기본적인 데이터 접근 기능이 자동으로 제공되므로, 개발자는 비즈니스 로직에만 집중할 수 있습니다.

## 장점

- 생산성: 반복적인 CRUD 메서드를 구현할 필요 없이 바로 사용 가능합니다.
- 유지보수 용이: Spring Data JPA가 제공하는 메서드를 활용하면 코드가 간결해지고 유지보수가 쉬워집니다.
- 확장성: 필요한 경우 커스텀 쿼리도 쉽게 작성할 수 있습니다.

## 단점

- 제어의 한계: 기본 메서드 외에 복잡한 로직이 필요한 경우 커스터마이징이 제한될 수 있습니다.
- Spring 종속성: Spring Data JPA에 의존하므로 다른 환경으로 이전하기 어렵습니다.

# 3. 두 방식의 비교

| **특성**             | **EntityManager**             | **JpaRepository**                    |
| -------------------- | ----------------------------- | ------------------------------------ |
| **사용 API**         | JPA 표준 `EntityManager`      | Spring Data JPA의 `JpaRepository`    |
| **CRUD 메서드 구현** | 직접 구현 필요                | 기본 메서드 자동 제공                |
| **유연성**           | 커스터마이징이 용이           | 메서드 이름 기반 쿼리 또는 JPQL 사용 |
| **생산성**           | 상대적으로 낮음               | 높음                                 |
| **러닝 커브**        | JPA 지식 요구                 | 상대적으로 쉬움                      |
| **복잡한 로직 처리** | JPQL 또는 Native Query로 구현 | 커스텀 쿼리 가능                     |

# 4. 언제 어떤 방식을 선택할까?

## EntityManager를 사용하는 것이 적합한 경우

- 프로젝트에서 데이터 접근 로직을 세밀하게 제어해야 하는 경우.
- JPA 표준 API를 활용하여 Spring 환경 외의 다른 환경에서도 코드를 재사용해야 하는 경우.
- 복잡한 쿼리나 비표준적인 데이터베이스 작업이 필요한 경우.

## JpaRepository를 사용하는 것이 적합한 경우

- 기본적인 CRUD 작업이 대부분인 경우.
- 빠르게 데이터 접근 계층을 구축하고 싶을 때.
- Spring Data JPA의 메서드 이름 기반 쿼리 생성 기능을 활용하고 싶은 경우.

# 마무리

EntityManager와 JpaRepository는 각각 장단점이 뚜렷하며, 프로젝트의 요구사항에 따라 선택해야 합니다. 간단한 CRUD 작업이 주가 되는 프로젝트에서는 JpaRepository를 사용하는 것이 생산성을 높이는 데 유리합니다. 반면, 복잡한 데이터 로직이 필요한 프로젝트에서는 EntityManager를 활용하여 세밀하게 제어할 수 있는 방법이 적합합니다.
