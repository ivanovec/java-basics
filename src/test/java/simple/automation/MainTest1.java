package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest1 {

    @Test
    void testObjectToString() {
        Main1 main = new Main1();
        // Test1 with various object types
        assertEquals("123", main.objectToString(123));
        assertEquals("true", main.objectToString(true));
        assertEquals("test", main.objectToString("test"));
        assertEquals("null", main.objectToString(null));
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
