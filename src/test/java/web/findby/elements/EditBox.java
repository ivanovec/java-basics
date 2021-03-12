package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditBox extends CustomElement{
    public EditBox(WebElement webElement, WebDriver driver) {
        super(webElement, driver);
    }

    public EditBox sendKeys(String text){
        waitVisibility().sendKeys(text);
        return this;
    }
}
