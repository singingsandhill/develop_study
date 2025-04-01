package com.market.product;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductEndpoint {

	@Value("${spring.application.name}")
	private String appName;

	@RabbitListener(queues = "${message.queue.product}")
	public void receiveMessage(String orderId){
		log.info("recieve OrderId: {}, appname: {}",orderId,appName);
	}

}
