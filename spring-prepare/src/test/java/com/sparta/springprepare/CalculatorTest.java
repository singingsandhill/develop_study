package com.sparta.springprepare;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    @DisplayName("더하기 테스트")
    public void test1() {
        //given
        Calculator calculator = new Calculator();
        Double result = calculator.operate(8,"+",2);
        System.out.println(result);

        //when
        Assertions.assertEquals(result,10.0);

        //then
    }

    @Test
    @DisplayName("나누기 테스트")
    public void test2() {
        //given
        Calculator calculator = new Calculator();
        Double result = calculator.operate(8,"/",2);
        System.out.println("result:"+result);
        //when
        Assertions.assertEquals(result,4.0);

        //then
    }

}