package org.example;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void setup() {
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        int result = calculator.add(5, 4);
        Assertions.assertEquals(9, result);
    }

    @Test
    public void testSubtract() {
        int result = calculator.subtract(5, 4);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testMultiply() {
        int result = calculator.multiply(5, 4);
        Assertions.assertEquals(20, result);
    }

    @Test
    public void testDivide() {
        int result = calculator.divide(5, 4);
        Assertions.assertEquals(1, result);
    }

    @AfterEach
    public void clean() {
        calculator = null;
    }

}
