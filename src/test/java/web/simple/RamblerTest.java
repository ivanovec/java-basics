package web.simple;

import com.google.common.io.Resources;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import web.simple.WebDriverFactory;
import web.simple.page.LoginPage;

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
                .isLoggedIn(email)
        );
    }

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
