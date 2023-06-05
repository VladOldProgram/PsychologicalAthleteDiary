package ru.pad;

import org.junit.Assert;
import org.junit.Test;

public class IntegrationTest {
    @Test
    public void emptyEmailIsNotValidForgotPasswordData() {
        Assert.assertEquals(-1, ForgotPasswordActivity.dataIsValid(""));
    }

    @Test
    public void notValidEmailIsNotValidForgotPasswordData() {
        Assert.assertEquals(-2, ForgotPasswordActivity.dataIsValid("example.com"));
    }

    @Test
    public void validEmailIsValidForgotPasswordData() {
        Assert.assertEquals(0, ForgotPasswordActivity.dataIsValid("mail@example.com"));
    }
}
