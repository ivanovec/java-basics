package web.findby.config;

import lombok.Data;
import web.findby.WebDriverFactory1;

@Data
public class WebConfig1 {
    private String baseUrl;
    private WebDriverFactory1.Browser browser;

    public void newStubMethod() {
        // TODO: implement
    }
}
