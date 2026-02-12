package web.simple.page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class ProfilePage1 extends BaseLoggedInPage1 {
    private By profilePageTitle = By.xpath("//h1[text()='Simple Automation']");

    public ProfilePage1(WebDriver driver) {
        super(driver);
    }

    @Step("Проверка, что открыта страница профиля")
    public boolean isProfileOpened() {
        try {
            return waitVisibility(profilePageTitle).isDisplayed();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }
}
