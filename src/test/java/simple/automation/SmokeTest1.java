package simple.automation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest1 {
    @Test
    void basicMath() {
        assertThat(2 + 2).isEqualTo(4);
    }
}
