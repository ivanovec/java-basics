package web.simple.page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class BaseLoggedInPage1 extends BasePage1 {
    private By loggedInEmail = By.xpath("//span[contains(text(), '@rambler.ru')]");
    private By profileLink = By.xpath("//a[text()='Мой профиль']");

    public BaseLoggedInPage1(WebDriver driver) {
        super(driver);
    }

    @Step("Проверка, что произошел логин под пользователем {0}")
    public boolean isLoggedIn(String email) {
        try {
            return waitVisibility(loggedInEmail).getText().equals(email);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Step("Открытие профиля")
    public ProfilePage1 openProfile() {
        waitAndClick(loggedInEmail);
        waitAndClick(profileLink);
        switchToAnotherTab();

        return new ProfilePage1(driver);
    }
}
