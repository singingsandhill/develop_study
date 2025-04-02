package com.kafkasaga.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentApplicationQueueConfig {
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		DefaultClassMapper classMapper = new DefaultClassMapper();

		Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("com.kafkasaga.product.DeliveryMessage", com.kafkasaga.payment.DeliveryMessage.class);

		classMapper.setIdClassMapping(idClassMapping);
		converter.setClassMapper(classMapper);

		return converter;
	}
}
