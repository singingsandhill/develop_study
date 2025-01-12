
### 도메인 모델 패턴이란?

**도메인 모델 패턴**은 소프트웨어의 도메인 지식을 코드에 직접 반영하여, 비즈니스 로직과 도메인 규칙을 명확히 표현하는 설계 방식입니다. 이 패턴은 복잡한 비즈니스 로직을 잘 구조화하고, 도메인 전문가와 개발자 간의 소통을 원활히 하며, 코드의 유지보수성을 향상시킵니다.


### 도메인 모델의 주요 구성 요소

도메인 모델은 보통 아래의 요소들로 구성됩니다:

1. **엔티티 (Entity)**: 고유 식별자를 가지며, 도메인의 핵심 데이터와 동작을 캡슐화.
2. **밸류 오브젝트 (Value Object)**: 값으로만 비교되며, 불변성을 가진 객체.
3. **애그리거트 (Aggregate)**: 도메인의 일관성을 보장하는 객체의 집합.
4. **리포지토리 (Repository)**: 엔티티를 저장하고 조회하는 책임.
5. **서비스 (Domain Service)**: 엔티티와 애그리거트가 해결하기 어려운 비즈니스 로직을 처리.

---

### 예제: 주문 도메인 모델

온라인 쇼핑몰의 주문 관리 시스템을 설계한다고 가정해 봅시다.

#### 1. 엔티티: `Order`

```java
public class Order {
    private Long orderId;  // 고유 식별자
    private List<OrderItem> items; // 주문 항목 목록
    private OrderStatus status; // 주문 상태
    private LocalDateTime orderDate; // 주문 날짜

    public Order(Long orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("완료된 주문은 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELED;
    }

    // Getter methods...
}
```

#### 2. 밸류 오브젝트: `OrderItem`

```java
public class OrderItem {
    private String productName;
    private int quantity;
    private Money price; // 밸류 객체로 금액 관리

    public OrderItem(String productName, int quantity, Money price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Money calculateTotalPrice() {
        return price.multiply(quantity);
    }

    // Getter methods...
}
```

#### 3. 밸류 오브젝트: `Money`

```java
import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
        this.amount = amount;
    }

    public Money multiply(int multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    // Getter and equals/hashCode...
}
```

#### 4. 리포지토리: `OrderRepository`

```java
import java.util.HashMap;
import java.util.Map;

public class OrderRepository {
    private final Map<Long, Order> orderStore = new HashMap<>();

    public void save(Order order) {
        orderStore.put(order.getOrderId(), order);
    }

    public Order findById(Long orderId) {
        return orderStore.get(orderId);
    }
}
```

#### 5. 도메인 서비스: 주문 총 금액 계산

```java
import java.util.List;

public class OrderService {
    public Money calculateTotalOrderAmount(Order order) {
        List<OrderItem> items = order.getItems();
        Money total = new Money(BigDecimal.ZERO);
        for (OrderItem item : items) {
            total = total.add(item.calculateTotalPrice());
        }
        return total;
    }
}
```

---

### 도메인 모델 패턴의 장점

1. **도메인 지식의 코드화**: 도메인 용어와 규칙이 코드에 녹아들어 이해가 쉽습니다.
2. **비즈니스 로직 분리**: 비즈니스 로직을 응용 계층에서 분리하여 재사용성과 유지보수성을 높입니다.
3. **유연성과 확장성**: 변경 요구사항에 유연하게 대응할 수 있습니다.

---

### 결론

도메인 모델 패턴은 복잡한 비즈니스 로직이 포함된 시스템에서 높은 가독성과 유지보수성을 제공합니다. 위의 예제는 단순한 주문 시스템을 보여주지만, 실제 프로젝트에서 이 패턴을 적용하면 도메인 지식을 효과적으로 관리할 수 있습니다.
