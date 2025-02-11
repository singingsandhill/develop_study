package com.spring_cloud.eureka.client.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ProductController {

    @Value("${server.port}")
    private String port;

//    @Value("${message}")
//    private String message;

//    @GetMapping("/product/{id}")
    @GetMapping("/product")
    public String getProduct(
//            @PathVariable("id") int id
    ) {
        return
//                "Product " + id+
                " info From port : " + port
//                        + " and message : " + message
                        ;
    }
}
