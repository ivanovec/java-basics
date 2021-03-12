package web.findby.config;

import lombok.Data;
import web.findby.WebDriverFactory;

@Data
public class WebConfig {
    private String baseUrl;
    private WebDriverFactory.Browser browser;
}
