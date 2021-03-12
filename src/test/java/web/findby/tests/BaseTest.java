package web.findby.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import web.findby.WebDriverFactory;

public class BaseTest {
    protected static final String email = "simpleautomation@rambler.ru";
    protected static final String password = "S1mpl34ut0m4ti0n";
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

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
}
