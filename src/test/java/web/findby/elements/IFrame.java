package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IFrame extends CustomElement{
    public IFrame(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public IFrame switchTo(){
        driver.switchTo().frame(waitVisibility());
        return this;
    }
}
