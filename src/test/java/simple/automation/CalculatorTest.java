package simple.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

	@Test
	void testAddition() {
		assertEquals (4, 2 + 2);
	}

	@Test
	void testSubtraction() {
		assertEquals (0, 2 - 2);
	}
}
