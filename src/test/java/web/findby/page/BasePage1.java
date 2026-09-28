package web.findby.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.findby.JSExecutor1;
import web.findby.elements.ElementsDecorator1;

import java.time.Duration;

public abstract class BasePage1 {
    protected static final String BASE_URL = "https://www.rambler.ru";
    private static final Duration DEFAULT_TIMEOUT_SECONDS = Duration.ofSeconds(10);

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JSExecutor1 js;

    public BasePage1(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
        this.js = new JSExecutor1(driver);

        ElementLocatorFactory factory = new DefaultElementLocatorFactory(driver);
        ElementsDecorator1 decorator = new ElementsDecorator1(factory, driver);
        PageFactory.initElements(decorator, this);
    }

    protected void waitAndClick(WebElement locator) {
//        new Actions(driver).click(wait.until(ExpectedConditions.elementToBeClickable(locator)));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected WebElement waitVisibility(WebElement locator) {
        return wait.until(ExpectedConditions.visibilityOf(locator));
    }

    public void switchToAnotherTab() {
        driver.switchTo().window(
                driver.getWindowHandles().stream()
                        .filter(h -> !h.equals(driver.getWindowHandle()))
                        .findFirst().get()
        );
    }
}
