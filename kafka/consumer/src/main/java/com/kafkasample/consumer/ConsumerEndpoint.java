package com.kafkasample.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsumerEndpoint {

	@KafkaListener(groupId = "group_a", topics = "topic1")
	public void consumerFromGroupA(String message) {
		log.info("Group A consumed message from topic1 : " + message);
	}

	@KafkaListener(groupId = "group_b", topics = "topic1")
	public void consumerFromGroupB(String message) {
		log.info("Group B consumed message from topic1 : " + message);
	}

	@KafkaListener(groupId = "group_c", topics = "topic2")
	public void consumerFromGroupC(String message) {
		log.info("Group C consumed message from topic2 : " + message);
	}

	@KafkaListener(groupId = "group_c", topics = "topic3")
	public void consumerFromGroupD(String message) {
		log.info("Group C consumed message from topic3 : " + message);
	}

	@KafkaListener(groupId = "group_d",topics = "topic4")
	public void consumerFromGroupE(String message){
		log.info("Group D consumed message from topic4 : "+message);

	}
}
