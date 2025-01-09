## 모든 연관관계는 지연 로딩(LAZY)으로 설정하자

엔티티 설계 시 연관관계의 로딩 전략은 성능 최적화와 유지보수성을 위해 매우 중요합니다.
특히 모든 연관관계를 지연 로딩(LAZY)으로 설정하는 것이 일반적인 권장 사항입니다. 즉시 로딩(EAGER)은 예측 불가능한 SQL 실행과 성능 문제를 초래할 수 있기 때문입니다.

---

### 1. 즉시 로딩(EAGER)의 문제점

#### 1.1. 예측 불가능한 SQL 실행

즉시 로딩(EAGER)은 엔티티를 조회할 때 연관된 모든 엔티티를 즉시 가져옵니다. 이로 인해 어떤 SQL이 실행될지 미리 예측하기 어려워지며, 개발 중 문제가 발생할 가능성이 높아집니다.

#### 1.2. N+1 문제

즉시 로딩은 연관된 엔티티를 각각 조회하기 때문에, 데이터 양이 많을수록 많은 추가 쿼리가 실행됩니다. 이를 N+1 문제라고 하며, 성능 저하의 주요 원인 중 하나입니다.

#### 1.3. 성능 저하

필요하지 않은 데이터까지 미리 조회하므로 메모리 사용량이 증가하고, 응답 시간이 느려질 수 있습니다. 특히 대규모 데이터셋을 다룰 때는 즉시 로딩이 성능 병목 현상을 초래할 가능성이 큽니다.

---

### 2. 지연 로딩(LAZY) 설정

지연 로딩(LAZY)은 실제로 데이터가 필요할 때 데이터를 조회하도록 설정하는 방식입니다. 이는 성능 최적화와 유지보수성을 크게 향상시킵니다.

#### 2.1. 기본 설정 방법

모든 연관관계에 대해 `fetch = FetchType.LAZY`를 명시적으로 설정합니다.

```java
@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
```

#### 2.2. 주의 사항

`@ManyToOne` 및 `@OneToOne` 관계의 기본 로딩 전략은 즉시 로딩(EAGER)입니다. 따라서, 이를 명시적으로 `fetch = FetchType.LAZY`로 변경해야 합니다.

---

### 3. 연관된 엔티티를 조회하는 방법

지연 로딩을 사용하면 필요한 데이터만 명시적으로 조회하도록 구현할 수 있습니다. 이를 위해 Fetch Join 또는 엔티티 그래프(Entity Graph)를 활용할 수 있습니다.

#### 3.1. Fetch Join 사용

Fetch Join은 JPQL에서 연관된 엔티티를 함께 조회하는 방법입니다.

```java
String jpql = "SELECT o FROM Order o JOIN FETCH o.member WHERE o.id = :orderId";
Order order = em.createQuery(jpql, Order.class)
                .setParameter("orderId", id)
                .getSingleResult();
```

#### 3.2. 엔티티 그래프 사용

엔티티 그래프는 Spring Data JPA에서 제공하는 기능으로, 연관된 엔티티를 동적으로 로딩할 수 있습니다.

```java
@EntityGraph(attributePaths = {"member", "delivery"})
@Query("SELECT o FROM Order o WHERE o.id = :orderId")
Order findOrderWithGraph(@Param("orderId") Long orderId);
```

---

### 4. 지연 로딩의 장점

1. **성능 최적화**: 필요한 데이터만 조회하므로 메모리 사용량과 응답 시간이 감소합니다.
2. **예측 가능성 향상**: 쿼리 실행 시점을 명확히 알 수 있어 디버깅과 유지보수가 용이합니다.
3. **유연한 로딩**: Fetch Join이나 엔티티 그래프를 사용하여 상황에 따라 데이터를 동적으로 로딩할 수 있습니다.
4. **N+1 문제 방지**: 연관된 데이터를 효율적으로 조회하여 성능 병목 현상을 줄입니다.

---

### 5. 결론

모든 연관관계를 지연 로딩(LAZY)으로 설정하는 것이 좋습니다. 즉시 로딩(EAGER)은 예측 불가능한 SQL 실행과 성능 문제를 초래하므로, 이를 지양해야 합니다.
필요한 경우 Fetch Join 또는 엔티티 그래프를 사용하여 명시적으로 데이터를 로드함으로써 성능 최적화와 코드 유지보수성을 동시에 달성할 수 있습니다.
