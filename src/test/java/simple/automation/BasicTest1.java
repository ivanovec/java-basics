package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicTest1 {

    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void testStringConcatenation() {
        String result = "Hello" + " " + "World";
        assertEquals("Hello World", result);
    }

    @Test
    void testNotNull() {
        String value = "Test";
        assertNotNull(value);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
