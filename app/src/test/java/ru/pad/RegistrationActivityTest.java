package ru.pad;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.pad.RegistrationActivity.dateFormatIsValid;
import static ru.pad.RegistrationActivity.emailFormatIsValid;
import static ru.pad.RegistrationActivity.dateIsNotFuture;
import static ru.pad.RegistrationActivity.passwordFormatIsValid;

public class RegistrationActivityTest {
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

    @Test
    public void correctStringIsValidDate() {
        assertTrue(dateFormatIsValid("05.06.2023"));
    }


    @Test
    public void incorrectOrderedStringIsNotValidDate() {
        assertFalse(dateFormatIsValid("2023.05.06"));
    }

    @Test
    public void emptyStringIsNotValidDate() {
        assertFalse(dateFormatIsValid(""));
    }

    @Test
    public void incorrectSeparatedStringIsNotValidDate() {
        assertFalse(dateFormatIsValid("05-06-2023"));
    }

    @Test
    public void pastDateIsNotFuture() {
        assertTrue(dateIsNotFuture("30.06.2002"));
    }

    @Test
    public void futureDateIsFuture() {
        assertFalse(dateIsNotFuture("30.06.9999"));
    }

    @Test
    public void correctStringIsValidPassword() {
        assertTrue(passwordFormatIsValid("password123"));
    }

    @Test
    public void correctStringWithSymbolIsValidPassword() {
        assertTrue(passwordFormatIsValid("!password1"));
    }

    @Test
    public void shortCorrectStringIsNotValidPassword() {
        assertFalse(passwordFormatIsValid("pass1"));
    }

    @Test
    public void longCorrectStringIsNotValidPassword() {
        assertFalse(passwordFormatIsValid("password123456789"));
    }

    @Test
    public void correctStringWithSpaceIsNotValidPassword() {
        assertFalse(passwordFormatIsValid("password 123"));
    }

    @Test
    public void incorrectStringWithoutCharIsNotValidPassword() {
        assertFalse(passwordFormatIsValid("1234567"));
    }

    @Test
    public void incorrectStringWithoutNumberIsNotValidPassword() {
        assertFalse(passwordFormatIsValid("password"));
    }

}