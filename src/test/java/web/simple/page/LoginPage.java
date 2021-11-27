package web.simple.page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage{
    private By openLogin = By.xpath("//*[text()='Вход']");
    private By loginIFrame = By.xpath("//div[@data-id-frame]/iframe");
    private By loginBox = By.id("login");
    private By passwordBox = By.id("password");
    private By processLogin = By.xpath("//*[text()='Войти']");

    public LoginPage(WebDriver driver) {
        super(driver);
        driver.get(BASE_URL);
        waitAndClick(openLogin);

        wait.until(ExpectedConditions.presenceOfElementLocated(loginIFrame));
        driver.switchTo().frame(driver.findElement(loginIFrame));

        waitVisibility(loginBox);
    }

    @Step("Ввод логина {email}")
    public LoginPage typeLogin(String email){
        driver.findElement(loginBox).sendKeys(email);
        return this;
    }

    @Step("Ввод Пароля {password}")
    public LoginPage typePassword(String password){
        driver.findElement(passwordBox).sendKeys(password);
        return this;
    }

    @Step("Нажатие кнопки логина")
    public SearchPage clickLogin(){
        driver.findElement(processLogin).click();
        driver.switchTo().window(driver.getWindowHandles().stream().findFirst().get())
        return new SearchPage(driver);
    }

    @Step("Логин как {0}:{1}")
    public SearchPage loginAs(String email, String password){
        typeLogin(email);
        typePassword(password);
        return clickLogin();
    }
}
