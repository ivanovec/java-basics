package web.findby.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage1 extends BaseLoggedInPage1 {
    public SearchPage1(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//img[contains(@src, 'news')]")
    private WebElement news;

    public SearchPage1 scrollToNews() {
        waitVisibility(loggedInEmail);
        js.scrollTo(news);
        return this;
    }
}
