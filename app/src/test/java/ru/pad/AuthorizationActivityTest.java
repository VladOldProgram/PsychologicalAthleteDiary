package ru.pad;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.pad.AuthorizationActivity.emailFormatIsValid;

public class AuthorizationActivityTest {
    @Test
    public void correctStringIsValidEmail() {
        assertTrue(emailFormatIsValid("example@example.com"));
    }

    @Test
    public void randomStringIsNotValidEmail() {
        assertFalse(emailFormatIsValid("example.com"));
    }

    @Test
    public void emptyStringIsNotValidEmail() {
        assertFalse(emailFormatIsValid(""));
    }
}