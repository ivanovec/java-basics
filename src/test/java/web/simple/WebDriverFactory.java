package web.simple;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import web.simple.config.TestConfigFactory;

import java.time.Duration;

public class WebDriverFactory {
    private static TestConfigFactory config = TestConfigFactory.getInstance();

    public static WebDriver getWebDriver(){
        switch (config.getWebConfig().getBrowser()){
            case "FIREFOX":
                return new FirefoxDriver();
            case "CHROME":
            default:
                return getChromeDriver();
        }
    }

    public enum Browser{
        CHROME, FIREFOX
    }

    private static ChromeDriver getChromeDriver(){
//            String chromeBinaryName;
        System.setProperty("webdriver.chrome.verboseLogging", "true");


//        switch(System.getProperty("os.name").toLowerCase()){
//            case "mac os x":
//                chromeBinaryName = "chromedriver";
//                break;
//            case "windows 7":
//                chromeBinaryName = "chromedriver.exe";
//                break;
//            case "linux":
//            default:
//                chromeBinaryName = "chromedriver_linux";
//                break;
//        }

//        System.setProperty("webdriver.chrome.driver", Resources.getResource(chromeBinaryName).getPath());
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(5));
        return driver;
    }
}
