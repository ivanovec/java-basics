package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorTest1 {

    @Test
    void testProcessData() {
        String input = "test";
        assertNotNull(input);
        assertEquals(4, input.length());
    }

    @Test
    void testEmptyData() {
        String empty = "";
        assertTrue(empty.isEmpty());
    }
}
