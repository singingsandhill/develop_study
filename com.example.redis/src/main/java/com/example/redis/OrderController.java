package com.example.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @PostMapping
    public ItemOrder create(@RequestBody ItemOrder order){
        return orderRepository.save(order);
    }

    @GetMapping
    public List<ItemOrder> readAll(){
        List<ItemOrder> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    @GetMapping("{id}")
    public ItemOrder readOne(@PathVariable String id){
        return orderRepository.findById(id).orElseThrow();
    }

    @PutMapping("{id")
    public ItemOrder update(@PathVariable("id") String id, @RequestBody ItemOrder order){
        return null;
    }
}
