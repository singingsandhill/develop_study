package com.kafkasaga.payment;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Payment {

	private UUID paymentId;
	private String userId;
	private Integer payAmount;
	private String payStatus;
}
