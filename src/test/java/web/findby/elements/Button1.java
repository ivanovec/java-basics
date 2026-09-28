package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button1 extends CustomElement1 {
    public Button1(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public void click() {
        waitClickability().click();
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
