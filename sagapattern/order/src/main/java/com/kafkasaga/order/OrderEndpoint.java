package com.kafkasaga.order;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderEndpoint {

	@RabbitListener(queues = "${message.queue.err.order}")
	public void errOrder(DeliveryMessage deliveryMessage) {
		log.info("ERROR RECEIVE!!!");
		orderService.rollbackOrder(deliveryMessage);
	}

	private final OrderService orderService;

	@GetMapping("order/{orderId}")
	public ResponseEntity<Order> getOrder(@PathVariable("orderId") UUID orderId) {
		Order order = orderService.getOrder(orderId);
		return ResponseEntity.ok(order);
	}

	@PostMapping("/order")
	public ResponseEntity<Order> order(@RequestBody OrderRequestDto orderRequsetDto) {
		Order order = orderService.createOrder(orderRequsetDto);
		return ResponseEntity.ok(order);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class OrderRequestDto {
		private String userId;
		private String productId;
		private Integer productQuantity;
		private Integer payAmount;

		public Order toOrder() {
			return Order.builder()
				.orderId(UUID.randomUUID())
				.userId(userId)
				.orderStatus("RECEIPT")
				.build();
		}

		public DeliveryMessage toDeliveryMessage(UUID orderId) {
			return DeliveryMessage.builder()
				.orderId(orderId)
				.userId(userId)
				.productId(Integer.valueOf(productId))
				.productQuantity(productQuantity)
				.payAmount(payAmount)
				.build();
		}
	}
}
