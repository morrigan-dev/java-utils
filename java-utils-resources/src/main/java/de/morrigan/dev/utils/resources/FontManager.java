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
 * Hier werden alle Schriftarten verwaltet, die für eine Anwendung benötigt werden. Da Anwendungen auf unterschiedlichen
 * Betriebssystemen laufen können, müssen sämtliche Schriftarten mit ausgeliefert werden und über diesen Manager geladen
 * und zur Verfügung gestellt werden.
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

    this.fontCache = new HashMap<>();
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
   */
  public void loadAllFontsFromResources() {
    loadAllFontsFromResources("");
  }

  /**
   * Lädt alle Schriftarten aus den resource Verzeichnissen, die auf dem Classpath liegen. Die Schriftarten müssen dabei
   * die Dateiendung .ttf besitzen und es muss sich um {@link Font#TRUETYPE_FONT} Schriftarten handeln.
   *
   * @param directory Pfad zu einem Unterverzeichnis beginnend bei resource in dem die Schrifarten liegen
   */
  public void loadAllFontsFromResources(String directory) {
    loadAllFontsFromResources(directory, Font.TRUETYPE_FONT, ".ttf");
  }

  /**
   * Lädt alle Schriftarten aus den resource Verzeichnissen, die auf dem Classpath liegen.
   *
   * @param directory Pfad zu einem Unterverzeichnis beginnend bei resource in dem die Schrifarten liegen
   * @param fontType Schrittyp (z.b. {@link Font#TRUETYPE_FONT})
   * @param fileExtension Dateiendung der Schriftarten (z.B. .ttf)
   */
  public void loadAllFontsFromResources(String directory, int fontType, String fileExtension) {
    Reflections reflections = new Reflections(directory, new ResourcesScanner());
    Set<String> availableFonts = reflections.getResources(Pattern.compile(".*\\" + fileExtension));

    int counter = 0;
    for (String fontPath : availableFonts) {
      String baseName = FilenameUtils.getBaseName(fontPath);
      try {
        addFont(baseName, fontType, fontPath);
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
    return this.fontCache.keySet();
  }

  /**
   * Löscht alle geladenen Schriftarten aus dem Cache.
   */
  public void clear() {
    this.fontCache.clear();
  }

  private void addFont(String key, int fontType, String filePath) throws FontFormatException, IOException {
    this.fontCache.put(key, Font.createFont(fontType, getClass().getResourceAsStream("/" + filePath)));
  }

  private Optional<Font> getFont(String fontName, Optional<Float> size, Optional<Integer> style,
      Optional<AffineTransform> trans) {
    Optional<Font> result = Optional.ofNullable(this.fontCache.get(fontName));
    if (result.isPresent()) {
      Font font = result.get();
      if (size.isPresent()) {
        font = font.deriveFont(size.get());
      }
      if (style.isPresent()) {
        font = font.deriveFont(style.get());
      }
      if (trans.isPresent()) {
        font = font.deriveFont(trans.get());
      }
      result = Optional.of(font);
    } else {
      LOG.warn("Font with name {} is not available!", fontName);
    }
    return result;
  }
}
