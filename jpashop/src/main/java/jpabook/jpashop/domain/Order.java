package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    /**
     * 주문 시간
     */
    private LocalDateTime orderDate;

    /**
     * 주문 상태
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 연관관계 메서드
     * 연관관계 메서드는 들고 있는 쪽에 있는 것이 좋다.
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /**
     * member.getOrders().add(this);로 연관관계 설정 완료
     */
//    public static void main(String[] args){
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
