package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button extends CustomElement{
    public Button(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public void click(){
        waitClickability().click();
    }
}
