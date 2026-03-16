package web.simple.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

public class TestConfigFactory1 {
    private volatile Config config;
    private volatile WebConfig1 webConfig;

    private TestConfigFactory1() {
        config = ConfigFactory.systemProperties()
                .withFallback(ConfigFactory.systemEnvironment())
                .withFallback(ConfigFactory.parseResources("test.conf"));
    }

    public synchronized WebConfig1 getWebConfig() {
        if (webConfig == null) {
            webConfig = ConfigBeanFactory.create(config.getConfig("web"), WebConfig1.class);
        }
        return webConfig;
    }

    public synchronized static TestConfigFactory1 getInstance() {
        return new TestConfigFactory1();
    }
}
