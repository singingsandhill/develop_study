package com.kafkasaga.product;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEndpoint {

	private final ProductService productService;

	@RabbitListener(queues = "${message.queue.product}")
	public void reciveMessage(DeliveryMessage deliveryMessage){
		log.info("Producr recieve: {}",deliveryMessage.toString());
		productService.reduceProductAmount(deliveryMessage);

	}

	@RabbitListener(queues = "${message.queue.err.product}")
	public void receiveErrorMessage(DeliveryMessage deliveryMessage){
		log.info("ERROR RECEIVE!!!");
		productService.rollbackProduct(deliveryMessage);

	}
}
