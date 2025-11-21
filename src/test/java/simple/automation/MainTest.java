package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testObjectToString() {
        Main main = new Main();
        // Test with various object types
        assertEquals("123", main.objectToString(123));
        assertEquals("true", main.objectToString(true));
        assertEquals("test", main.objectToString("test"));
        assertEquals("null", main.objectToString(null));
    }
}
