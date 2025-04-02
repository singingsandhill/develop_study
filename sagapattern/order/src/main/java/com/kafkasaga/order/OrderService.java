package com.kafkasaga.order;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private  final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.product}")
	private String productQueue;

	private Map<UUID,Order> orderStore = new HashMap<>();

	public Order createOrder(OrderEndpoint.OrderRequestDto orderRequestDto){
		Order order = orderRequestDto.toOrder();
		orderStore.put(order.getOrderId(), order);

		// message queue에 전달
		DeliveryMessage deliveryMessage = orderRequestDto.toDeliveryMessage(order.getOrderId());
		rabbitTemplate.convertAndSend(productQueue, deliveryMessage);

		return order;
	}

	public Order getOrder(UUID orderId){
		return orderStore.get(orderId);
	}

	public void rollbackOrder(DeliveryMessage deliveryMessage) {
		Order order = orderStore.get(deliveryMessage.getOrderId());
		order.cancelOrder(deliveryMessage.getErrorType());
		log.info("rollback is completed");
	}
}
