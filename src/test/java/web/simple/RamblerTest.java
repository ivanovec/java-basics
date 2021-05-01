package web.simple;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import web.simple.page.LoginPage;

public class RamblerTest {

    private static final String email = "simpleautomation@rambler.ru";
    private static final String password = "S1mpl34ut0m4ti0n";

    @AfterEach
    public void closeDriver(){
        Selenide.closeWebDriver();
    }

    @Test
    public void login(){
        Assertions.assertTrue(
                new LoginPage()
                .loginAs(email, password)
                .isLoggedIn(email)
        );
    }

    @Test
    public void openProfile(){
        Assertions.assertTrue(
                new LoginPage()
                        .loginAs(email, password)
                        .openProfile()
                .isProfileOpened()
        );
    }
}
