package simple;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NewTest {

    @Test
    public void testExample() {
        // Simple test case
        int expected = 5;
        int actual = 2 + 3;
        assertEquals(expected, actual, "2 + 3 should equal 5");
    }

    @Test
    public void testStringConcatenation() {
        String result = "Hello" + " " + "World";
        assertEquals("Hello World", result);
    }
}
