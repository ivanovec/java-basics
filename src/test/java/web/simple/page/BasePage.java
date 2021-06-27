package web.simple.page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected static final String BASE_URL = "https://www.rambler.ru";
    private static final Duration DEFAULT_TIMEOUT_SECONDS = Duration.ofSeconds(10);

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
    }

    protected void waitAndClick(By locator){
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    @Step("Ожидание элемента с локатором {locator}")
    protected WebElement waitVisibility(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Переключение на другой tab")
    public void switchToAnotherTab(){
        driver.switchTo().window(
                driver.getWindowHandles().stream()
                        .filter(h -> !h.equals(driver.getWindowHandle()))
                        .findFirst().get()
        );
    }
}
