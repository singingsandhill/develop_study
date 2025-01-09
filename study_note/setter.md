## 엔티티에서 Setter 사용을 지양해야 하는 이유와 리팩토링 방법

엔티티 클래스는 객체지향적으로 데이터를 표현하며, 비즈니스 로직을 포함할 수 있는 핵심 구성 요소입니다. 
하지만, 엔티티 클래스에 Setter 메서드를 사용하는 것은 유지보수성과 코드의 안정성 측면에서 여러 가지 문제를 초래할 수 있습니다.

### 1. Setter 사용의 문제점

#### 1.1. 변경 포인트의 분산
Setter 메서드가 열려 있으면 객체의 상태를 변경할 수 있는 지점이 코드 전반에 걸쳐 분산됩니다. 
이는 다음과 같은 문제를 야기합니다:
- 객체의 상태가 언제, 어디서, 어떻게 변경되는지 추적하기 어렵습니다.
- 잘못된 상태로 객체가 변경될 가능성이 높아집니다.

#### 1.2. 캡슐화 위반
엔티티는 비즈니스 로직과 데이터를 캡슐화하여 외부로부터 보호해야 합니다. 하지만 Setter 메서드는 외부에서 엔티티의 상태를 직접 변경할 수 있는 통로를 제공합니다. 
이는 객체지향 설계의 원칙을 위반하며, 코드의 안정성을 저하시킬 수 있습니다.

#### 1.3. 유지보수 비용 증가
Setter 메서드가 많으면 객체의 상태를 변경하는 코드가 여러 곳에 산재하게 되어, 변경 사항을 적용하거나 디버깅할 때 유지보수 비용이 증가합니다. 
또한, 리팩토링 시 영향 범위를 정확히 예측하기 어려워질 수 있습니다.

### 2. Setter를 제거하는 방법

Setter를 제거하려면, 상태 변경을 제어할 수 있는 다른 방법을 도입해야 합니다. 이를 위해 다음과 같은 접근 방식을 고려할 수 있습니다.

#### 2.1. 생성자 활용
객체 생성 시 필요한 데이터를 생성자를 통해 초기화합니다. 불변성이 요구되는 필드는 생성자에서만 설정하도록 합니다.

```java
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 생성자
    public Order(LocalDateTime orderDate, OrderStatus status) {
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getter만 제공
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
```

#### 2.2. 의도를 드러내는 메서드 추가
Setter 대신 비즈니스 로직에 맞는 메서드를 제공하여 객체의 상태를 변경합니다. 이를 통해 코드의 가독성을 높이고, 상태 변경 로직을 중앙 집중화할 수 있습니다.

```java
public void cancelOrder() {
    if (this.status == OrderStatus.COMPLETED) {
        throw new IllegalStateException("이미 완료된 주문은 취소할 수 없습니다.");
    }
    this.status = OrderStatus.CANCELLED;
}

public void completeOrder() {
    this.status = OrderStatus.COMPLETED;
}
```

#### 2.3. 불변 객체 활용
가능한 경우, 엔티티를 불변 객체로 설계합니다. 필드 값을 변경할 수 없도록 하고, 상태 변경 시 새로운 객체를 생성하도록 합니다.

```java
@Entity
public class Customer {
    private final String name;
    private final String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Customer updateEmail(String newEmail) {
        return new Customer(this.name, newEmail);
    }
}
```

### 3. Setter 제거 후의 장점

1. **객체의 상태 관리 용이**: 상태 변경이 명확히 정의된 메서드를 통해 이루어지므로, 객체의 상태를 관리하기 쉽습니다.
2. **코드 가독성 향상**: 의도를 드러내는 메서드를 통해 비즈니스 로직을 쉽게 이해할 수 있습니다.
3. **유지보수성 증가**: 상태 변경 로직이 집중되어 있어 디버깅과 리팩토링이 용이합니다.
4. **불변성 보장**: 불변 객체는 동시성 문제를 줄이고 코드 안정성을 높입니다.

### 4. 결론

엔티티 클래스에서 Setter 사용은 유지보수성과 코드의 안정성을 저하시킬 수 있는 주요 원인 중 하나입니다. 
Setter를 제거하고 생성자와 의도를 드러내는 메서드를 활용하면 객체지향적인 설계 원칙을 준수하고, 유지보수성이 높은 코드를 작성할 수 있습니다. 

