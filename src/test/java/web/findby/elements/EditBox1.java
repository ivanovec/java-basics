package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditBox1 extends CustomElement1 {
    public EditBox1(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public EditBox1 sendKeys(String text) {
        waitVisibility().sendKeys(text);
        return this;
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
