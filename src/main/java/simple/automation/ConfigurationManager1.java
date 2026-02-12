package simple.automation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages application configuration settings.
 */
public class ConfigurationManager1 {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager1.class.getName());

    private final Map<String, String> settings;
    private static ConfigurationManager1 instance;

    private ConfigurationManager1() {
        this.settings = new HashMap<>();
        loadDefaults();
    }

    /**
     * Gets the singleton instance of ConfigurationManager1.
     *
     * @return the ConfigurationManager1 instance
     */
    public static synchronized ConfigurationManager1 getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager1();
        }
        return instance;
    }

    /**
     * Loads default configuration settings.
     */
    private void loadDefaults() {
        settings.put("app.name", "User1 Management System");
        settings.put("app.version", "1.0.0");
        settings.put("log.level", "INFO");
        LOGGER.info("Default configuration loaded");
    }

    /**
     * Gets a configuration value.
     *
     * @param key the configuration key
     * @return Optional containing the value if found
     */
    public Optional<String> get(String key) {
        return Optional.ofNullable(settings.get(key));
    }

    /**
     * Sets a configuration value.
     *
     * @param key   the configuration key
     * @param value the configuration value
     */
    public void set(String key, String value) {
        settings.put(key, value);
        LOGGER.info("Configuration updated: " + key + " = " + value);
    }

    /**
     * Gets all configuration settings.
     *
     * @return map of all settings
     */
    public Map<String, String> getAllSettings() {
        return new HashMap<>(settings);
    }
}
