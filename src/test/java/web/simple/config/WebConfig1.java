package web.simple.config;

import lombok.Data;
import web.simple.WebDriverFactory1;

@Data
public class WebConfig1 {
    private String baseUrl;
    private WebDriverFactory1.Browser browser;
}
