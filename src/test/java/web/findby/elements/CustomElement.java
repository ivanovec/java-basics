package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class CustomElement {
    private static final Duration DEFAULT_TIMEOUT_SECONDS = Duration.ofSeconds(10);
    protected WebElement webElement;
    protected WebDriver driver;
    protected WebDriverWait wait;

    public CustomElement(WebElement webElement, WebDriver driver){
        this.webElement = webElement;
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT_SECONDS);
    }

    public WebElement waitVisibility(){
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public WebElement getWebElement(){
        return webElement;
    }

    public String getText(){
        return webElement.getText();
    }

    protected WebElement waitClickability(){
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}
