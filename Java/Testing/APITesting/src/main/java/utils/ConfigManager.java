package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    static{
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            logger.info("Loading config file...");
            if(input == null) {
                logger.error("Could not find config.properties");
            }else {
                logger.info("Loaded config file...");
                properties.load(input);
            }
        }catch (IOException io) {
            logger.error("Could not load config.properties {}", io.getMessage());
        }
    }

    public static String getAPiKey(KeyType keyType) {
        String key = properties.getProperty(keyType.getApiKey());
        if(key == null && key.trim().isEmpty()) {
            logger.error("Missing api key");
            throw new IllegalArgumentException("API key not found for: " + keyType.getApiKey());
        }
        logger.info("API key found: " + keyType.getApiKey());
        return key.trim();
    }
}
