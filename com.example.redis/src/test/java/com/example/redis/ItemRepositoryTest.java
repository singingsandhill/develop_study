package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void createTest(){
        Item item = Item.builder()
                .id(1L)
                .name("키보드")
                .description("기계식")
                .price(100000)
                .build();

        itemRepository.save(item);
    }

    @Test
    public void readOneTest(){
        Item item = itemRepository.findById(1L).orElseThrow();
        System.out.println(item.getDescription());
    }

    @Test
    public void updateTest(){
        Item item = itemRepository.findById(1L)
                .orElseThrow();
        item.setDescription("판매중");
        item = itemRepository.save(item);
        System.out.println(item.getDescription());
    }

    @Test
    public void deleteTest(){
        itemRepository.deleteById(1L);
    }
}