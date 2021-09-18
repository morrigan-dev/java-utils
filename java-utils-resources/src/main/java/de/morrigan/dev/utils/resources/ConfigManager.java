package de.morrigan.dev.utils.resources;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

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

  private ConfigManager() {
    super();

    this.configs = new Properties();
  }

  public Optional<String> getConfig(String configKey) {
    return getConfigInternal(configKey);
  }

  public void loadAllConfigsFromResources(String filename) throws IOException {
    this.configs.load(getClass().getResourceAsStream(StringUtils.join("/", filename)));
  }

  public Set<String> getConfigKeys() {
    return this.configs.stringPropertyNames();
  }

  public void clear() {
    this.configs.clear();
  }

  private Optional<String> getConfigInternal(String configKey) {
    String value = this.configs.getProperty(configKey);
    return value == null ? Optional.empty() : Optional.of(value);
  }
}
