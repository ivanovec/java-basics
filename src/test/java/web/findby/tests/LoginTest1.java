package web.findby.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import web.findby.page.LoginPage1;

public class LoginTest1 extends BaseTest1 {
    @Test
    public void login() {
        Assertions.assertTrue(
                new LoginPage1(driver.get())
                        .loginAs(email, password)
                        .isLoggedInAs(email)
        );
    }
}
