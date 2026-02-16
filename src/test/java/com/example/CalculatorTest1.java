package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest1 {

    @Test
    void testAddition() {
        int result = 2 + 2;
        assertEquals(4, result);
    }

    @Test
    void testSubtraction() {
        int result = 5 - 3;
        assertEquals(2, result);
    }

    @Test
    void testMultiplication() {
        int result = 3 * 5;
        assertEquals(12, result);
    }

    @Test
    void testDivision() {
        int result = 10 / 2;
        assertEquals(5, result);
    }

    // Stub method
    @Test
    void testStub() {
        // This is a placeholder for future implementation
        fail("Not implemented yet");
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
