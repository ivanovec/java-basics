package web.findby.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import web.findby.elements.Button1;
import web.findby.elements.EditBox1;
import web.findby.elements.IFrame1;

public class LoginPage1 extends BasePage1 {
    @FindBy(xpath = "//*[text()='Вход']")
    private Button1 openLogin;
    @FindBy(xpath = "//div[@data-id-frame]/iframe")
    private IFrame1 loginIFrame;
    @FindBy(id = "login")
    private EditBox1 loginBox;
    @FindBy(id = "password")
    private EditBox1 passwordBox;
    @FindBy(xpath = "//*[text()='Войти']")
    private Button1 processLogin;

    public LoginPage1(WebDriver driver) {
        super(driver);
        driver.get(BASE_URL);
        openLogin.click();
        loginIFrame.switchTo();
        loginBox.waitVisibility();
    }

    public LoginPage1 typeLogin(String email) {
        loginBox.sendKeys(email);
        return this;
    }

    public LoginPage1 typePassword(String password) {
        passwordBox.sendKeys(password);
        return this;
    }

    public SearchPage1 clickLogin() {
        processLogin.click();
        return new SearchPage1(driver);
    }

    public SearchPage1 loginAs(String email, String password) {
        typeLogin(email);
        typePassword(password);
        return clickLogin();
    }
}
