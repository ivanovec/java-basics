package web.simple.page;

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

    public LoginPage typeLogin(String email){
        driver.findElement(loginBox).sendKeys(email);
        return this;
    }

    public LoginPage typePassword(String password){
        driver.findElement(passwordBox).sendKeys(password);
        return this;
    }

    public SearchPage clickLogin(){
        driver.findElement(processLogin).click();
        return new SearchPage(driver);
    }

    public SearchPage loginAs(String email, String password){
        typeLogin(email);
        typePassword(password);
        return clickLogin();
    }
}
