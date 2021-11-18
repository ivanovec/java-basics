package web.simple;

import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import web.simple.page.LoginPage;

import java.net.URL;

@DisplayName("Базовые тесты Rambler")
@ExtendWith(ScreenshotExtension.class)
public class RamblerTest {

    private static final String email = "simpleautomation@rambler.ru";
    private static final String password = "S1mpl34ut0m4ti0n";
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static Logger log = LogManager.getLogger(RamblerTest.class);

    @BeforeEach
    public void createDriver(){
        WebDriver driverO = WebDriverFactory.getWebDriver();
        log.info("driver is " + driverO);
        driver.set(WebDriverFactory.getWebDriver());
        log.info("driver was set");
    }

//    @AfterEach
//    public void disposeDriver(){
//        if(driver.get() != null){
//            driver.get().quit();
//        }
//    }

    @Test
    @DisplayName("Проверка логина")
    public void login(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                .loginAs(email, password)
                .isLoggedIn(email)
        );
    }

    @Test
    @DisplayName("Открытие профиля")
    public void openProfile(){
        Assertions.assertTrue(
                new LoginPage(driver.get())
                        .loginAs(email, password)
                        .openProfile()
                .isProfileOpened()
        );
    }
}
