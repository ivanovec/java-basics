package simple.automation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringUtilsTest {

    @Test
    void isEmptyHandlesNullAndEmpty() {
        assertEquals(true, StringUtils.isEmpty(null));
        assertEquals(true, StringUtils.isEmpty(""));
        assertEquals(false, StringUtils.isEmpty(" "));
    }

    @Test
    void isBlankTreatsWhitespaceAsBlank() {
        assertEquals(true, StringUtils.isBlank(null));
        assertEquals(true, StringUtils.isBlank(""));
        assertEquals(true, StringUtils.isBlank("   "));
        assertEquals(false, StringUtils.isBlank(" a "));
    }

    @Test
    void truncateAddsEllipsisWhenNeeded() {
        assertEquals("hello", StringUtils.truncate("hello", 5));
        assertEquals("he...", StringUtils.truncate("hello", 2));
        assertNull(StringUtils.truncate(null, 3));
    }

    @Test
    void capitalizeNormalizesCase() {
        assertEquals("Hello", StringUtils.capitalize("hELLo"));
        assertEquals("", StringUtils.capitalize(""));
        assertNull(StringUtils.capitalize(null));
    }

    @Test
    void reverseHandlesNullAndStrings() {
        assertEquals("cba", StringUtils.reverse("abc"));
        assertNull(StringUtils.reverse(null));
    }

    @Test
    void splitHandlesDelimitersAndNull() {
        assertEquals(List.of("a", "b", "c"), StringUtils.split("a,b,c", ','));
        assertEquals(List.of(""), StringUtils.split("", ','));
        assertEquals(List.of(), StringUtils.split(null, ','));
    }

    @Test
    void countOccurrencesFindsNonOverlappingMatches() {
        assertEquals(2, StringUtils.countOccurrences("ababab", "ab"));
        assertEquals(0, StringUtils.countOccurrences("ababab", "aba"));
        assertEquals(0, StringUtils.countOccurrences(null, "a"));
    }
}
