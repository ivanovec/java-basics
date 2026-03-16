package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest1 {

    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void testSubtraction() {
        assertEquals(0, 2 - 2);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
