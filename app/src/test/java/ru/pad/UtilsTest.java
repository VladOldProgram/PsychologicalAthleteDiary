package ru.pad;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilsTest {
    @Test
    public void correctStringIsValidEmail() {
        assertTrue(Utils.emailFormatIsValid("example@example.com"));
    }

    @Test
    public void randomStringIsNotValidEmail() {
        assertFalse(Utils.emailFormatIsValid("example.com"));
    }

    @Test
    public void emptyStringIsNotValidEmail() {
        assertFalse(Utils.emailFormatIsValid(""));
    }
}
