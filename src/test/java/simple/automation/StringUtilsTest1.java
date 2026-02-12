package simple.automation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringUtilsTest1 {

    @Test
    void isEmptyHandlesNullAndEmpty() {
        assertEquals(true, StringUtils1.isEmpty(null));
        assertEquals(true, StringUtils1.isEmpty(""));
        assertEquals(false, StringUtils1.isEmpty(" "));
    }

    @Test
    void isBlankTreatsWhitespaceAsBlank() {
        assertEquals(true, StringUtils1.isBlank(null));
        assertEquals(true, StringUtils1.isBlank(""));
        assertEquals(true, StringUtils1.isBlank("   "));
        assertEquals(false, StringUtils1.isBlank(" a "));
    }

    @Test
    void truncateAddsEllipsisWhenNeeded() {
        assertEquals("hello", StringUtils1.truncate("hello", 5));
        assertEquals("he...", StringUtils1.truncate("hello", 2));
        assertNull(StringUtils1.truncate(null, 3));
    }

    @Test
    void capitalizeNormalizesCase() {
        assertEquals("Hello", StringUtils1.capitalize("hELLo"));
        assertEquals("", StringUtils1.capitalize(""));
        assertNull(StringUtils1.capitalize(null));
    }

    @Test
    void reverseHandlesNullAndStrings() {
        assertEquals("cba", StringUtils1.reverse("abc"));
        assertNull(StringUtils1.reverse(null));
    }

    @Test
    void splitHandlesDelimitersAndNull() {
        assertEquals(List.of("a", "b", "c"), StringUtils1.split("a,b,c", ','));
        assertEquals(List.of(""), StringUtils1.split("", ','));
        assertEquals(List.of(), StringUtils1.split(null, ','));
    }

    @Test
    void countOccurrencesFindsNonOverlappingMatches() {
        assertEquals(2, StringUtils1.countOccurrences("ababab", "ab"));
        assertEquals(0, StringUtils1.countOccurrences("ababab", "aba"));
        assertEquals(0, StringUtils1.countOccurrences(null, "a"));
    }
}
