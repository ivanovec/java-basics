package web.simple.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.io.Resources;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.codeborne.selenide.Selenide.switchTo;

public abstract class BasePage {
    protected static final String BASE_URL = "https://www.rambler.ru";
    public BasePage(){
        Configuration.timeout = Duration.of(1, ChronoUnit.MINUTES).toMillis();
//        System.setProperty("browser", "firefox");
//        System.setProperty("webdriver.chrome.driver", Resources.getResource("chromedriver").getPath());
    }

    protected void waitAndClick(SelenideElement element){
        element.shouldBe(Condition.enabled).click();
    }

    protected SelenideElement waitVisibility(SelenideElement element){
        return element.shouldBe(Condition.visible);
    }

    public void switchToAnotherTab(){
        WebDriver driver = WebDriverRunner.getWebDriver();
        switchTo().window(
                driver.getWindowHandles().stream()
                        .filter(h -> !h.equals(driver.getWindowHandle()))
                        .findFirst().get()
        );
    }
}
