package de.morrigan.dev.utils.resources;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageManager {

  /** Logger für Debug/Fehlerausgaben */
  private static final Logger LOG = LoggerFactory.getLogger(LanguageManager.class);

  public static LanguageManager getInstance() {
    return INSTANCE;
  }

  private static final LanguageManager INSTANCE = new LanguageManager();

  private enum Bundle {
    LABELS, MESSAGES, ERRORS
  }

  private Map<Locale, ResourceBundle> labels;
  private Map<Locale, ResourceBundle> messages;
  private Map<Locale, ResourceBundle> errors;

  private LanguageManager() {
    super();
    this.labels = new HashMap<>();
    this.messages = new HashMap<>();
    this.errors = new HashMap<>();
  }

  public String getLabel(String key, Locale locale) {
    return getValue(Bundle.LABELS, key, locale);
  }

  public String getLabel(String key) {
    return getValue(Bundle.LABELS, key, Locale.getDefault());
  }

  public void loadLabelsFromResources(String baseName, Locale locale) {
    loadLabelsFromResources(baseName, Optional.of(locale));
  }

  public void loadLabelsFromResources(String baseName) {
    loadLabelsFromResources(baseName, Optional.empty());
  }

  public Set<String> getLabelKeys() {
    return getLabelKeys(Locale.getDefault());
  }

  public Set<String> getLabelKeys(Locale locale) {
    Set<String> result = new HashSet<>();
    ResourceBundle labelsBundle = getResourceBundle(Bundle.LABELS, locale);
    Enumeration<String> keys = labelsBundle.getKeys();
    while (keys.hasMoreElements()) {
      result.add(keys.nextElement());
    }
    return result;
  }

  private String getValue(Bundle bundleName, String key, Locale locale) {
    String result = "";
    try {
      ResourceBundle resourceBundle = getResourceBundle(bundleName, locale);
      result = resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      LOG.warn("No value found for the key '{}' in the resource bundle '{}'. Details: {}", key, bundleName.name(),
          e.getMessage());
    }
    return result;
  }

  private ResourceBundle getResourceBundle(Bundle bundle, Locale locale) {
    ResourceBundle resourceBundle;
    switch (bundle) {
      case LABELS:
        resourceBundle = this.labels.get(locale);
      break;
      case MESSAGES:
        resourceBundle = this.messages.get(locale);
      break;
      case ERRORS:
        resourceBundle = this.errors.get(locale);
      break;

      default:
        throw new IllegalStateException("Missing mapping for bundle: " + bundle);
    }
    if (resourceBundle == null) {
      throw new MissingResourceException("No resource bundle available for the language " + locale.getDisplayLanguage()
          + ". Please use a load method for your language before receiving a value.", bundle.name(), "");
    }
    return resourceBundle;
  }

  private void loadLabelsFromResources(String baseName, Optional<Locale> optLocale) {
    if (optLocale.isPresent()) {
      Locale locale = optLocale.get();
      this.labels.put(locale, ResourceBundle.getBundle(baseName, locale));
    } else {
      this.labels.put(Locale.getDefault(), ResourceBundle.getBundle(baseName));
    }
  }

}
