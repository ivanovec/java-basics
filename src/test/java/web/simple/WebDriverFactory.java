package web.simple;

import com.google.common.io.Resources;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import web.simple.config.TestConfigFactory;

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
        if(System.getProperty("webdriver.chrome.driver").isEmpty()) {
            String chromeBinaryName;
            switch (System.getProperty("os.name").toLowerCase()) {
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
            System.out.println(chromeBinaryName);

            System.setProperty("webdriver.chrome.driver", Resources.getResource(chromeBinaryName).getPath());
        }

        return new ChromeDriver();
    }
}
