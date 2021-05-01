package web.simple.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage extends BaseLoggedInPage{
    private SelenideElement profilePageTitle = $x("//h1[text()='Simple Automation']");

    public boolean isProfileOpened(){
        try {
            return waitVisibility(profilePageTitle).isDisplayed();
        } catch (NoSuchElementException e){
            e.printStackTrace();
            return false;
        }
    }
}
