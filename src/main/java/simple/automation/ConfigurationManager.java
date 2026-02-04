package simple.automation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages application configuration settings.
 */
public class ConfigurationManager {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class.getName());
    
    private final Map<String, String> settings;
    private static ConfigurationManager instance;
    
    private ConfigurationManager() {
        this.settings = new HashMap<>();
        loadDefaults();
    }
    
    /**
     * Gets the singleton instance of ConfigurationManager.
     *
     * @return the ConfigurationManager instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Loads default configuration settings.
     */
    private void loadDefaults() {
        settings.put("app.name", "User Management System");
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
     * @param key the configuration key
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
