package com.spring_cloud.eureka.client.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderSrvice orderService;

    @GetMapping("/order/{orderId}")
//    @GetMapping("order")
    public String getOrder(
            @PathVariable("orderId") String orderId
    ) {
        return orderService.getOrder(orderId);
//        return "Order detail";
    }
}
