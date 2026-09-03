package vn.edu.hcmute.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JpaConfig {
    public static final String PERSISTENCE_UNIT = "category-crud-pu";
    private static final EntityManagerFactory FACTORY = createFactory();

    private JpaConfig() {
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        Properties localProperties = loadLocalProperties();
        override(properties, localProperties.getProperty("JPA_DB_URL"), "jakarta.persistence.jdbc.url");
        override(properties, localProperties.getProperty("JPA_DB_USER"), "jakarta.persistence.jdbc.user");
        override(properties, localProperties.getProperty("JPA_DB_PASSWORD"), "jakarta.persistence.jdbc.password");
        overrideFromEnvironment(properties, "JPA_DB_URL", "jakarta.persistence.jdbc.url");
        overrideFromEnvironment(properties, "JPA_DB_USER", "jakarta.persistence.jdbc.user");
        overrideFromEnvironment(properties, "JPA_DB_PASSWORD", "jakarta.persistence.jdbc.password");
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
    }

    private static Properties loadLocalProperties() {
        Properties properties = new Properties();
        Path configFile = Path.of(System.getProperty("user.home"), ".baitap.properties");
        if (!Files.isRegularFile(configFile)) {
            return properties;
        }
        try (InputStream input = Files.newInputStream(configFile)) {
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể đọc file cấu hình database cục bộ", exception);
        }
    }

    private static void overrideFromEnvironment(Map<String, Object> properties, String environmentName,
            String persistenceProperty) {
        override(properties, System.getenv(environmentName), persistenceProperty);
    }

    private static void override(Map<String, Object> properties, String value, String persistenceProperty) {
        if (value != null && !value.isBlank()) {
            properties.put(persistenceProperty, value);
        }
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
