package web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static com.codeborne.selenide.Selenide.*;

public class SelenideBasicTests {

    @Test
    public void openRambler(){
        open("https://www.rambler.ru");
        $x("//*[text()='Вход']").shouldBe(Condition.enabled).click();
        switchTo().frame($x("//div[@data-id-frame]/iframe"));
        $("#login").shouldBe(Condition.visible, Duration.of(1, ChronoUnit.MINUTES));
    }
}
