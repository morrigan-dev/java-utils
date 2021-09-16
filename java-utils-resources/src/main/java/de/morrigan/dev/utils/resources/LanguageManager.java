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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Hier werden alle Beschriftungen und Texte verwaltet, die für eine Anwendung benötigt werden. Da Anwendungen häufig
 * mehrsprachig angeboten werden, unterstützt dieser Manager mittels {@link ResourceBundle} Mehrsprachigkeit.
 *
 * @author morrigan
 */
public class LanguageManager {

  /** Logger für Debug/Fehlerausgaben */
  private static final Logger LOG = LoggerFactory.getLogger(LanguageManager.class);

  /**
   * @return einzige Instanz dieses Managers.
   */
  public static LanguageManager getInstance() {
    return INSTANCE;
  }

  private static final String COLON = ":";

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

  /**
   * Ermittelt zu einem Beschriftungsschlüssel eine entsprechende Beschriftung und liefert diese zurück. Die
   * Beschriftung wird in der Sprache zurückgegeben, die als Default ({@link Locale#getDefault()}) in der JVM hinterlegt
   * ist.
   *
   * @param key Beschriftungsschlüssel
   * @return eine Beschriftung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getLabel(String key) {
    return getValue(Bundle.LABELS, key, Locale.getDefault());
  }

  /**
   * Ermittelt zu einem Beschriftungsschlüssel eine entsprechende Beschriftung und liefert diese zurück. An die
   * Beschriftung wird ein Doppelpunkt angehangen. Die Beschriftung wird in der Sprache zurückgegeben, die als Default
   * ({@link Locale#getDefault()}) in der JVM hinterlegt ist.
   *
   * @param key Beschriftungsschlüssel
   * @return eine Beschriftung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getLabelWithColon(String key) {
    String value = getValue(Bundle.LABELS, key, Locale.getDefault());
    return StringUtils.isBlank(value) ? value : StringUtils.join(value, COLON);
  }

  /**
   * Ermittelt zu einem Beschriftungsschlüssel eine entsprechende Beschriftung und liefert diese in der angegebenen
   * Sprache zurück.
   *
   * @param key Beschriftungsschlüssel
   * @param locale Sprachspezifischer Ort
   * @return eine Beschriftung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getLabel(String key, Locale locale) {
    return getValue(Bundle.LABELS, key, locale);
  }

  /**
   * Ermittelt zu einem Beschriftungsschlüssel eine entsprechende Beschriftung und liefert diese in der angegebenen
   * Sprache zurück. An die Beschriftung wird ein Doppelpunkt angehangen.
   *
   * @param key Beschriftungsschlüssel
   * @param locale Sprachspezifischer Ort
   * @return eine Beschriftung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getLabelWithColon(String key, Locale locale) {
    String value = getValue(Bundle.LABELS, key, locale);
    return StringUtils.isBlank(value) ? value : StringUtils.join(value, COLON);
  }

  /**
   * Ermittelt zu einem Nachrichtenschlüssel eine entsprechende Nachricht und liefert diese zurück. Die Nachricht wird
   * in der Sprache zurückgegeben, die als Default ({@link Locale#getDefault()}) in der JVM hinterlegt ist. Sofern die
   * Nachricht Platzhalter für Parameter besitzt werden diese mit den angegebenen Parametern befüllt. (see
   * {@link MessageFormatter})
   *
   * @param key Nachrichtenschlüssel
   * @param parameters Parameter, die statt der Platzhalter in der Nachricht eingefügt werden
   * @return eine Nachricht oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getMessage(String key, Object... parameters) {
    return replacePlaceholder(getValue(Bundle.MESSAGES, key, Locale.getDefault()), parameters);
  }

  /**
   * Ermittelt zu einem Beschriftungsschlüssel eine entsprechende Beschriftung und liefert diese in der angegebenen
   * Sprache zurück. Sofern die Nachricht Platzhalter für Parameter besitzt werden diese mit den angegebenen Parametern
   * befüllt. (see {@link MessageFormatter})
   *
   * @param key Nachrichtenschlüssel
   * @param locale Sprachspezifischer Ort
   * @param parameters Parameter, die statt der Platzhalter in der Nachricht eingefügt werden
   * @return eine Beschriftung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getMessage(String key, Locale locale, Object... parameters) {
    return replacePlaceholder(getValue(Bundle.MESSAGES, key, locale), parameters);
  }

  /**
   * Ermittelt zu einem Fehlerschlüssel eine entsprechende Fehlerbeschreibung und liefert diese zurück. Die
   * Fehlerbeschreibung wird in der Sprache zurückgegeben, die als Default ({@link Locale#getDefault()}) in der JVM
   * hinterlegt ist. Sofern die Fehlerbeschreibung Platzhalter für Parameter besitzt werden diese mit den angegebenen
   * Parametern befüllt. (see {@link MessageFormatter})
   *
   * @param key Fehlerschlüssel
   * @param parameters Parameter, die statt der Platzhalter in der Fehlerbeschreibung eingefügt werden
   * @return eine Fehlerbeschreibung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getError(String key, Object... parameters) {
    return replacePlaceholder(getValue(Bundle.ERRORS, key, Locale.getDefault()), parameters);
  }

  /**
   * Ermittelt zu einem Fehlerschlüssel eine entsprechende Fehlerbeschreibung und lieder diese in der angegebenen
   * Sprache zurück. Sofern die Fehlerbeschreibung Platzhalter für Parameter besitzt werden diese mit den angegebenen
   * Parametern befüllt. (see {@link MessageFormatter})
   *
   * @param key Fehlerschlüssel
   * @param locale Sprachspezifischer Ort
   * @param parameters Parameter, die statt der Platzhalter in der Fehlerbeschreibung eingefügt werden
   * @return eine Fehlerbeschreibung oder ein Leerstring, falls es zu dem Schlüssel keine Beschriftung gibt.
   */
  public String getError(String key, Locale locale, Object... parameters) {
    return replacePlaceholder(getValue(Bundle.ERRORS, key, locale), parameters);
  }

  /**
   * Lädt alle Beschriftungen aus der angegebenen Ressource und stellt diese in diesem Manager zur Verfügung. Es wird
   * eine Ressource für die aktuelle Default-Sprache ({@link Locale#getDefault()}) gesucht. Wird diese nicht gefunden,
   * wird versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadLabelsFromResources(String baseName) {
    loadLabelsFromResources(Bundle.LABELS, baseName, Optional.empty());
  }

  /**
   * Lädt alle Beschriftungen aus der angegebenen Ressource und stellt diese in diesem Manager in der angegebenen
   * Sprache zur Verfügung. Wird diese nicht gefunden, wird versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @param locale Sprachspezifischer Ort
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadLabelsFromResources(String baseName, Locale locale) {
    loadLabelsFromResources(Bundle.LABELS, baseName, Optional.of(locale));
  }

  /**
   * Lädt alle Nachrichten aus der angegebenen Ressource und stellt diese in diesem Manager zur Verfügung. Es wird eine
   * Ressource für die aktuelle Default-Sprache ({@link Locale#getDefault()}) gesucht. Wird diese nicht gefunden, wird
   * versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadMessagesFromResources(String baseName) {
    loadLabelsFromResources(Bundle.MESSAGES, baseName, Optional.empty());
  }

  /**
   * Lädt alle Nachrichten aus der angegebenen Ressource und stellt diese in diesem Manager in der angegebenen Sprache
   * zur Verfügung. Wird diese nicht gefunden, wird versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @param locale Sprachspezifischer Ort
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadMessagesFromResources(String baseName, Locale locale) {
    loadLabelsFromResources(Bundle.MESSAGES, baseName, Optional.of(locale));
  }

  /**
   * Lädt alle Fehlerbeschreibungen aus der angegebenen Ressource und stellt diese in diesem Manager zur Verfügung. Es
   * wird eine Ressource für die aktuelle Default-Sprache ({@link Locale#getDefault()}) gesucht. Wird diese nicht
   * gefunden, wird versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadErrorsFromResources(String baseName) {
    loadLabelsFromResources(Bundle.ERRORS, baseName, Optional.empty());
  }

  /**
   * Lädt alle Fehlerbeschreibungen aus der angegebenen Ressource und stellt diese in diesem Manager in der angegebenen
   * Sprache zur Verfügung. Wird diese nicht gefunden, wird versucht eine Standard Ressource zu laden.<br>
   * Es wird nur nach Ressourcen auf dem Classpath gesucht.
   *
   * @param baseName Name/Pfad zu einer Ressource
   * @param locale Sprachspezifischer Ort
   * @throws MissingResourceException falls die angegebene Ressource nicht gefunden werden kann.
   * @see ResourceBundle#getBundle(String, Locale)
   */
  public void loadErrorsFromResources(String baseName, Locale locale) {
    loadLabelsFromResources(Bundle.ERRORS, baseName, Optional.of(locale));
  }

  /**
   * @return alle Beschriftungsschlüssel, die von diesem Manager in der Standard-Sprache verwaltet werden
   */
  public Set<String> getLabelKeys() {
    return getLabelKeys(Locale.getDefault());
  }

  /**
   * @param locale Sprachspezifischer Ort
   * @return alle Beschriftungsschlüssel, die von diesem Manager zur angegebenen Sprache verwaltet werden
   */
  public Set<String> getLabelKeys(Locale locale) {
    return toSet(getResourceBundle(Bundle.LABELS, locale));
  }

  /**
   * @return alle Nachrichtenschlüssel, die von diesem Manager in der Standard-Sprache verwaltet werden
   */
  public Set<String> getMessageKeys() {
    return getMessageKeys(Locale.getDefault());
  }

  /**
   * @param locale Sprachspezifischer Ort
   * @return alle Nachrichtenschlüssel, die von diesem Manager zur angegebenen Sprache verwaltet werden
   */
  public Set<String> getMessageKeys(Locale locale) {
    return toSet(getResourceBundle(Bundle.MESSAGES, locale));
  }

  /**
   * @return alle Fehlerschlüssel, die von diesem Manager in der Standard-Sprache verwaltet werden
   */
  public Set<String> getErrorKeys() {
    return getErrorKeys(Locale.getDefault());
  }

  /**
   * @param locale Sprachspezifischer Ort
   * @return alle Fehlerschlüssel, die von diesem Manager zur angegebenen Sprache verwaltet werden
   */
  public Set<String> getErrorKeys(Locale locale) {
    return toSet(getResourceBundle(Bundle.ERRORS, locale));
  }

  /**
   * Löscht alle im Cache vorhandenen Beschriftungen, Nachrichten und Fehlerbeschreibungen für alle Sprachen.
   */
  public void clear() {
    this.labels = new HashMap<>();
    this.messages = new HashMap<>();
    this.errors = new HashMap<>();
  }

  private Set<String> toSet(ResourceBundle bundle) {
    Set<String> result = new HashSet<>();
    Enumeration<String> keys = bundle.getKeys();
    while (keys.hasMoreElements()) {
      result.add(keys.nextElement());
    }
    return result;
  }

  private String replacePlaceholder(String value, Object... parameters) {
    return MessageFormatter.arrayFormat(value, parameters).getMessage();
  }

  private String getValue(Bundle bundleName, String key, Locale locale) {
    String result = "";
    try {
      if (!StringUtils.isBlank(key)) {
        ResourceBundle resourceBundle = getResourceBundle(bundleName, locale);
        result = resourceBundle.getString(key);
      }
    } catch (MissingResourceException e) {
      LOG.warn("No value found for the key '{}' in the resource bundle '{}'. Details: {}", key, bundleName.name(),
          e.getMessage());
    }
    return result;
  }

  private ResourceBundle getResourceBundle(Bundle bundle, Locale locale) {
    ResourceBundle resourceBundle;
    if (locale == null) {
      locale = Locale.getDefault();
    }
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

  private void loadLabelsFromResources(Bundle bundle, String baseName, Optional<Locale> optLocale) {
    Locale locale;
    if (optLocale.isPresent()) {
      locale = optLocale.get();
    } else {
      locale = Locale.getDefault();
    }
    switch (bundle) {
      case LABELS:
        this.labels.put(locale, ResourceBundle.getBundle(baseName, locale));
      break;
      case MESSAGES:
        this.messages.put(locale, ResourceBundle.getBundle(baseName, locale));
      break;
      case ERRORS:
        this.errors.put(locale, ResourceBundle.getBundle(baseName, locale));
      break;

      default:
        throw new IllegalStateException("Missing mapping for bundle: " + bundle);
    }
  }
}
