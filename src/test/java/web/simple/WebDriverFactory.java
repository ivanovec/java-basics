package web.simple;

import com.google.common.io.Resources;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import web.simple.config.TestConfigFactory;

import java.time.Duration;

public class WebDriverFactory {
    private static TestConfigFactory config = TestConfigFactory.getInstance();

    public static WebDriver getWebDriver(){
        switch (config.getWebConfig().getBrowser()){
            case FIREFOX:
                return new FirefoxDriver();
            case CHROME:
            default:
                return getChromeDriver();
        }
    }

    public enum Browser{
        CHROME, FIREFOX
    }

    private static ChromeDriver getChromeDriver(){
        String chromeBinaryName;

        switch(System.getProperty("os.name").toLowerCase()){
            case "mac os x":
            default:
                chromeBinaryName = "chromedriver";
                break;
            case "windows 7":
                chromeBinaryName = "chromedriver.exe";
                break;
            case "linux":
                chromeBinaryName = "chromedriver_linux";
                break;
        }

        System.setProperty("webdriver.chrome.driver", Resources.getResource(chromeBinaryName).getPath());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-features=VizDisplayCompositor");
//        options.addArguments("--headless", "--no-gpu");

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(5));
        return driver;
    }
}
