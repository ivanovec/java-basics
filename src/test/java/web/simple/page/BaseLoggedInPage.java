package web.simple.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;

import static com.codeborne.selenide.Selenide.$x;

public class BaseLoggedInPage extends BasePage{
    private SelenideElement loggedInEmail = $x("//span[contains(text(), '@rambler.ru')]");
    private SelenideElement profileLink = $x("//a[text()='Мой профиль']");

    public boolean isLoggedIn(String email){
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

        return new ProfilePage();
    }
}
