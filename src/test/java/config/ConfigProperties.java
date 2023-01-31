package config;

import java.util.Properties;

public class ConfigProperties {
    Properties properties;

    public ConfigProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config/config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperties(String propertyName) {
        return properties.getProperty(propertyName);
    }
}