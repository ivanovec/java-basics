package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IFrame1 extends CustomElement1 {
    public IFrame1(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public IFrame1 switchTo() {
        driver.switchTo().frame(waitVisibility());
        return this;
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
