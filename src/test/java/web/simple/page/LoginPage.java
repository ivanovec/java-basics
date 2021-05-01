package web.simple.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage extends BasePage{
    private SelenideElement openLogin = $x("//*[text()='Вход']");
    private SelenideElement loginIFrame = $x("//div[@data-id-frame]/iframe");
    private SelenideElement loginBox = $("#login");
    private SelenideElement passwordBox = $("#password");
    private SelenideElement processLogin = $x("//*[text()='Войти']");

    public LoginPage() {
        open(BASE_URL);
        waitAndClick(openLogin);
        switchTo().frame(loginIFrame.shouldBe(Condition.visible));
        waitVisibility(loginBox);
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
        return new SearchPage();
    }

    public SearchPage loginAs(String email, String password){
        typeLogin(email);
        typePassword(password);
        return clickLogin();
    }
}
