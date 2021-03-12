package web.simple.config;

import lombok.Data;
import web.simple.WebDriverFactory;

@Data
public class WebConfig {
    private String baseUrl;
    private WebDriverFactory.Browser browser;
}
