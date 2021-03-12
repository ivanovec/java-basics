package web.findby;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import web.findby.page.LoginPage;

public class RamblerTest {

    private static final String email = "simpleautomation@rambler.ru";
    private static final String password = "S1mpl34ut0m4ti0n";
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeEach
    public void createDriver(){
        driver.set(WebDriverFactory.getWebDriver());
    }

    @AfterEach
    public void disposeDriver(){
        if(driver.get() != null){
            driver.get().quit();
        }
    }

    @Test
    public void login(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                .loginAs(email, password)
                .isLoggedInAs(email)
        );
    }

    @Test
    public void openProfile(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                        .loginAs(email, password)
//                        .scrollToNews()
                        .openProfile()
                .isProfileOpened()
        );
    }
}
