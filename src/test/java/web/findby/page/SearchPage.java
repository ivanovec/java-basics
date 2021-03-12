package web.findby.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends BaseLoggedInPage {
    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//img[contains(@src, 'news')]")
    private WebElement news;

    public SearchPage scrollToNews(){
        waitVisibility(loggedInEmail);
        js.scrollTo(news);
        return this;
    }
}
