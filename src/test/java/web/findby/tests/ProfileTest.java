package web.findby.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import web.findby.page.LoginPage;

public class ProfileTest extends BaseTest{
    @Test
    public void openProfile(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                        .loginAs(email, password)
                        .openProfile()
                        .isProfileOpened()
        );
    }
}
