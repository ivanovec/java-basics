package web.findby.page;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BaseLoggedInPage extends BasePage {
    @FindBy(xpath = "//span[contains(text(), '@rambler.ru')]")
    protected WebElement loggedInEmail;
    @FindBy(xpath = "//a[text()='Мой профиль']")
    protected WebElement profileLink;

    public BaseLoggedInPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoggedInAs(String email){
        try {
            return waitVisibility(loggedInEmail).getText().equals(email);
        } catch (NoSuchElementException e){
            e.printStackTrace();
            return false;
        }
    }

    public ProfilePage openProfile(){
        waitAndClick(loggedInEmail);
        waitAndClick(profileLink);
        switchToAnotherTab();

        return new ProfilePage(driver);
    }
}
