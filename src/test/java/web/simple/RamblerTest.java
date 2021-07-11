package web.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import web.simple.page.LoginPage;

@DisplayName("Базовые тесты Rambler")
@ExtendWith(ScreenshotExtension.class)
public class RamblerTest {

    private static final String email = "simpleautomation@rambler.ru";
    private static final String password = "S1mpl34ut0m4ti0n";
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeEach
    public void createDriver(){
        driver.set(WebDriverFactory.getWebDriver());
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
