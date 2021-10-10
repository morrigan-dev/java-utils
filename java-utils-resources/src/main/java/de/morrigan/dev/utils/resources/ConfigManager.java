package de.morrigan.dev.utils.resources;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

/**
 * Hier werden alle Konfigurationen verwaltet, die für eine Anwendung benötigt werden.
 *
 * @author morrigan
 */
public class ConfigManager {

   /**
    * @return einzige Instanz dieses Managers.
    */
   public static ConfigManager getInstance() {
      return INSTANCE;
   }

   private static final ConfigManager INSTANCE = new ConfigManager();

   private Properties configs;
   private String configFilename;

   private ConfigManager() {
      super();

      this.configs = new Properties();
   }

   public Optional<String> getOptConfig(String configKey) {
      return getOptConfigInternal(configKey);
   }

   public String getConfig(String configKey) {
      return getConfigInternal(configKey);
   }

   public String getConfig(String configKey, Object... parameters) {
      return replacePlaceholder(getConfigInternal(configKey), parameters);
   }

   public void loadAllConfigsFromResources(String filename) throws IOException {
      this.configFilename = filename;
      this.configs.load(getClass().getResourceAsStream(StringUtils.join("/", filename)));
   }

   public Set<String> getConfigKeys() {
      return this.configs.stringPropertyNames();
   }

   public void clear() {
      this.configs.clear();
   }

   private String replacePlaceholder(String value, Object... parameters) {
      return MessageFormatter.arrayFormat(value, parameters).getMessage();
   }

   private String getConfigInternal(String configKey) {
      Optional<String> optConfigValue = getOptConfigInternal(configKey);
      if (optConfigValue.isPresent()) {
         return optConfigValue.get();
      } else {
         throw new IllegalArgumentException(MessageFormatter.arrayFormat("Requested configuration with the key {} is not present in the configuration file {}",
                  new Object[] { configKey, this.configFilename }).getMessage());
      }
   }

   private Optional<String> getOptConfigInternal(String configKey) {
      String value = this.configs.getProperty(configKey);
      return value == null ? Optional.empty() : Optional.of(value);
   }
}
