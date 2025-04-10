package com.kafkasaga.payment;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

	private final RabbitTemplate rabbitTemplate;
	@Value("${message.queue.err.product}")
	private String productErrorQueue;

	public void creatPayment(DeliveryMessage deliveryMessage) {
		Payment payment = Payment.builder()
			.paymentId(UUID.randomUUID())
			.userId(deliveryMessage.getUserId())
			.payAmount(deliveryMessage.getPayAmount())
			.payStatus("SUCCESS")
			.build();

		Integer payAmount = deliveryMessage.getPayAmount();

		if(payment.getPayAmount() >= 10000){
			log.error("Payment amount exceeds limiy : {}",payAmount);
			deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEED");
			this.rollbackPayment(deliveryMessage);
		}
	}

	public void rollbackPayment(DeliveryMessage deliveryMessage){
		log.info("PAYMENT ROLLBACK");
		rabbitTemplate.convertAndSend(productErrorQueue,deliveryMessage);

	}
}
