package de.morrigan.dev.utils.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Diese Klasse verwaltet alle Schriftarten, die für diese Anwendung benötigt werden. Da Anwendungen auf
 * unterschiedlichen Betriebssystemen laufen können, müssen sämtliche Schriftarten mit ausgeliefert werden und über
 * diesen Manager geladen und zur Verfügung gestellt werden.
 * 
 * @author morrigan
 */
public class FontManager {

  /** Logger für Debug/Fehlerausgaben */
  private static final Logger LOG = LoggerFactory.getLogger(FontManager.class);

  /**
   * @return einzige Instanz dieses Managers.
   */
  public static FontManager getInstance() {
    return INSTANCE;
  }

  private static final FontManager INSTANCE = new FontManager();

  private Map<String, Font> fontCache;

  private FontManager() {
    super();

    fontCache = new HashMap<>();
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese zurück.
   * 
   * @param fontName Name einer Schriftart
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName) {
    return getFont(fontName, Optional.empty(), Optional.empty(), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in der angegebenen Größe zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param size Größe einer Schriftart ({@link Font#deriveFont(float)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, float size) {
    return getFont(fontName, Optional.of(size), Optional.empty(), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in dem angegebenen Style zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param style Style einer Schriftart ({@link Font#deriveFont(int)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, int style) {
    return getFont(fontName, Optional.empty(), Optional.of(style), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese mit der angegebenen Transformation zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param trans Transformation einer Schriftart ({@link Font#deriveFont(AffineTransform)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, AffineTransform trans) {
    return getFont(fontName, Optional.empty(), Optional.empty(), Optional.of(trans));
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in der angegebenen Größe und Style zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param size Größe einer Schriftart ({@link Font#deriveFont(float)} wird genutzt)
   * @param style Style einer Schriftart ({@link Font#deriveFont(int)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, float size, int style) {
    return getFont(fontName, Optional.of(size), Optional.of(style), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in der angegebenen Größe und Transformation zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param size Größe einer Schriftart ({@link Font#deriveFont(float)} wird genutzt)
   * @param trans Transformation einer Schriftart ({@link Font#deriveFont(AffineTransform)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, float size, AffineTransform trans) {
    return getFont(fontName, Optional.of(size), Optional.empty(), Optional.of(trans));
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in dem angegebenen Style und Transformation zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param style Style einer Schriftart ({@link Font#deriveFont(int)} wird genutzt)
   * @param trans Transformation einer Schriftart ({@link Font#deriveFont(AffineTransform)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, int style, AffineTransform trans) {
    return getFont(fontName, Optional.empty(), Optional.of(style), Optional.of(trans));
  }

  /**
   * Prüft die Verfügbarkeit einer Schriftart und liefert diese in der angegebenen Größe, Style und Transformation
   * zurück.
   * 
   * @param fontName Name einer Schriftart
   * @param size Größe einer Schriftart ({@link Font#deriveFont(float)} wird genutzt)
   * @param style Style einer Schriftart ({@link Font#deriveFont(int)} wird genutzt)
   * @param trans Transformation einer Schriftart ({@link Font#deriveFont(AffineTransform)} wird genutzt)
   * @return eine Schriftart
   */
  public Optional<Font> getFont(String fontName, float size, int style, AffineTransform trans) {
    return getFont(fontName, Optional.of(size), Optional.of(style), Optional.of(trans));
  }

  /**
   * Lädt alle Schriftarten aus den resource Verzeichnissen, die auf dem Classpath liegen. Die Schriftarten müssen dabei
   * die Dateiendung .ttf besitzen und es muss sich um {@link Font#TRUETYPE_FONT} Schriftarten handeln.
   * 
   * @throws FontFormatException falls die Dateien mit den Schriftarten beschädigt ist
   * @throws IOException falls es beim Lesen der Dateien mit den Schriftarten zu Fehlern kommt
   */
  public void loadAllFontsFromResources() {
    loadAllFontsFromResources("");
  }

  /**
   * Lädt alle Schriftarten aus den resource Verzeichnissen, die auf dem Classpath liegen. Die Schriftarten müssen dabei
   * die Dateiendung .ttf besitzen und es muss sich um {@link Font#TRUETYPE_FONT} Schriftarten handeln.
   * 
   * @param directory Ein Pfad zu einem Unterverzeichnis beginnend bei resource in dem die Schrifarten liegen
   * @throws FontFormatException falls die Dateien mit den Schriftarten beschädigt ist
   * @throws IOException falls es beim Lesen der Dateien mit den Schriftarten zu Fehlern kommt
   */
  public void loadAllFontsFromResources(String directory) {
    Reflections reflections = new Reflections(directory, new ResourcesScanner());
    Set<String> availableFonts = reflections.getResources(Pattern.compile(".*\\.ttf"));

    int counter = 0;
    for (String font : availableFonts) {
      String baseName = FilenameUtils.getBaseName(font);
      try {
        addFont(baseName, Font.TRUETYPE_FONT, font);
        counter++;
      } catch (FontFormatException | IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    LOG.info("{} Schriftarten erfolgreich geladen...", counter);
  }

  /**
   * Alle Keys zu denen eine Font gefunden und in diesem Manager hinterlegt wurde. Mit diesen Keys können gezielt
   * einzelne Fonts abgerufen werden.
   * 
   * @return eine Menge von Keys.
   */
  public Set<String> getFontKeys() {
    return fontCache.keySet();
  }

  /**
   * Löscht alle geladenen Schriftarten aus dem Cache.
   */
  public void clear() {
    fontCache.clear();
  }

  private void addFont(String key, int fontType, String filename) throws FontFormatException, IOException {
    fontCache.put(key, Font.createFont(fontType, getClass().getResourceAsStream("/" + filename)));
  }

  private Optional<Font> getFont(String fontName, Optional<Float> size, Optional<Integer> style,
      Optional<AffineTransform> trans) {
    Optional<Font> font = Optional.ofNullable(fontCache.get(fontName));
    if (font.isPresent()) {
      Font origFont = font.get();
      if (size.isPresent()) {
        origFont = origFont.deriveFont(size.get());
      }
      if (style.isPresent()) {
        origFont = origFont.deriveFont(style.get());
      }
      if (trans.isPresent()) {
        origFont = origFont.deriveFont(trans.get());
      }
      font = Optional.of(origFont);
    } else {
      LOG.warn("Font with name {} is not available!", fontName);
    }
    return font;
  }
}
