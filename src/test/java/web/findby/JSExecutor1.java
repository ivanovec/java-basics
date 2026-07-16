package web.findby;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSExecutor1 {

    private JavascriptExecutor executor;

    public JSExecutor1(WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
    }

    public void scrollTo(WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
