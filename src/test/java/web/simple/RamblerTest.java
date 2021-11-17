package web.simple;

import com.google.common.io.Resources;
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

    @BeforeEach
    public void createDriver(){
        URL resource =  Resources.getResource( "chromedriver_linux");
        String path = resource.getPath();
        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--disable-gpu", "--no-sandbox");

        driver.set(new ChromeDriver(chromeOptions));
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
