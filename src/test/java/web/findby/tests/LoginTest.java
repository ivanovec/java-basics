package web.findby.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import web.findby.page.LoginPage;

public class LoginTest extends BaseTest{
    @Test
    public void login(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                        .loginAs(email, password)
                        .isLoggedInAs(email)
        );
    }
}
