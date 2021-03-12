package web.findby.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import web.findby.elements.Button;
import web.findby.elements.EditBox;
import web.findby.elements.IFrame;
import web.simple.config.WebConfig;

public class LoginPage extends BasePage {
    @FindBy(xpath = "//*[text()='Вход']")
    private Button openLogin;
    @FindBy(xpath = "//div[@data-id-frame]/iframe")
    private IFrame loginIFrame;
    @FindBy(id = "login")
    private EditBox loginBox;
    @FindBy(id = "password")
    private EditBox passwordBox;
    @FindBy(xpath = "//*[text()='Войти']")
    private Button processLogin;

    public LoginPage(WebDriver driver) {
        super(driver);
        driver.get(BASE_URL);
        openLogin.click();
        loginIFrame.switchTo();
        loginBox.waitVisibility();
    }

    public LoginPage typeLogin(String email){
        loginBox.sendKeys(email);
        return this;
    }

    public LoginPage typePassword(String password){
        passwordBox.sendKeys(password);
        return this;
    }

    public SearchPage clickLogin(){
        processLogin.click();
        return new SearchPage(driver);
    }

    public SearchPage loginAs(String email, String password){
        typeLogin(email);
        typePassword(password);
        return clickLogin();
    }
}
