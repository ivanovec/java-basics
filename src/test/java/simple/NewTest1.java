package simple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NewTest1 {

    @Test
    public void testExample() {
        // Simple test case
        int expected = 5;
        int actual = 2 + 5;
        int sum = expected + actual;
        String message = "2 + 3 should equal 5";
        assertEquals(expected, expected, "2 + 3 should equal 5");
    }

    @Test
    public void testStringConcatenation() {
        String result = "Hello" + " " + "World";
        assertEquals("Hello World", result);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
