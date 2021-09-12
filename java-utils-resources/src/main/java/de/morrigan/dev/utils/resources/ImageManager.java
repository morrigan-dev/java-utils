package de.morrigan.dev.utils.resources;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hier werden alle Bilder verwaltet, die in einer Anwendung genutzt werden sollen. Die Bilder werden in einen Cache
 * geladen und können dann nach Bedarf abgerufen und angezeigt werden.
 * <p>
 * Aktuell werden folgende Bildformate unterstützt.
 * <ul>
 * <li>Joint Photographic Experts Group (JPG/JEPG)</li>
 * <li>Graphics Interchange Format (GIF)</li>
 * <li>Portable Network Graphics (PNG)</li>
 * <li>Tagged Image File Format (TIF/TIFF)</li>
 * <li>Windows Bitmap (BMP)</li>
 * <li>Microsoft Icon (ICO)</li>
 * </ul>
 *
 * @author morrigan
 */
public class ImageManager {

  /** Logger für Debug/Fehlerausgaben */
  private static final Logger LOG = LoggerFactory.getLogger(ImageManager.class);

  public static ImageManager getInstance() {
    return INSTANCE;
  }

  /** Einzige Instanz dieses Managers */
  private static final ImageManager INSTANCE = new ImageManager();

  /** Unterstützte Dateiendungen */
  private static final String[] SUPPORTED_FILE_EXTENSIONS = new String[] {
      ".bmp", ".gif", ".ico", ".jpg", ".jpeg", ".png", ".tif", ".tiff",
      ".BMP", ".GIF", ".ICO", ".JPG", ".JPEG", ".PNG", ".TIF", ".TIFF"
  };

  /** Beinhaltet alle geladenen Bilder und können über einen entsprechenden Schlüssel abgerufen werden */
  private final Map<String, Image> imageCache;

  private ImageManager() {
    super();

    this.imageCache = new HashMap<>();
  }

  /**
   * Prüft die Verfügbarkeit eines Bildes und liefert dieses zurück.
   *
   * @param imageName Name eines Bildes
   * @return ein Bild
   */
  public Optional<Image> getImage(String imageName) {
    return getImage(imageName, Optional.empty(), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit eines Bildes und liefert dieses in der angegebenen Größe zurück.
   *
   * @param imageName Name eines Bildes
   * @param scaleToWidth Skalierungsfaktor in der Breite
   * @param scaleToHeight Skalierungsfaktor in der Höhe
   * @return ein Bild
   */
  public Optional<Image> getImage(String imageName, int scaleToWidth, int scaleToHeight) {
    return getImage(imageName, Optional.of(scaleToWidth), Optional.of(scaleToHeight));
  }

  /**
   * Prüft die Verfügbarkeit eines Bildes und liefert dieses in der angegebenen Größe zurück.
   *
   * @param imageName Name eines Bildes
   * @param scaleToDimension Skalierungsfaktor
   * @return ein Bild
   */
  public Optional<Image> getImage(String imageName, Dimension scaleToDimension) {
    return getImage(imageName, Optional.of(scaleToDimension.width), Optional.of(scaleToDimension.height));
  }

  /**
   * Prüft die Verfügbarkeit eines Icons und liefert dieses zurück.
   *
   * @param imageName name eines Icons
   * @return ein Icon
   */
  public Optional<ImageIcon> getImageIcon(String imageName) {
    return getImageIcon(imageName, Optional.empty(), Optional.empty());
  }

  /**
   * Prüft die Verfügbarkeit eines Icons und liefert dieses in der angegebenen Größe zurück.
   *
   * @param imageName Name eines Icons
   * @param scaleToWidth Skalierungsfaktor in der Breite
   * @param scaleToHeight Skalierungsfaktor in der Höhe
   * @return ein Icon
   */
  public Optional<ImageIcon> getImageIcon(String imageName, int scaleToWidth, int scaleToHeight) {
    return getImageIcon(imageName, Optional.of(scaleToWidth), Optional.of(scaleToHeight));
  }

  /**
   * Prüft die Verfügbarkeit eines Icons und liefert dieses in der angegebenen Größe zurück.
   *
   * @param imageName Name eines Icons
   * @param scaleToDimension Skalierungsfaktor
   * @return ein Icon
   */
  public Optional<ImageIcon> getImageIcon(String imageName, Dimension scaleToDimension) {
    return getImageIcon(imageName, Optional.of(scaleToDimension.width), Optional.of(scaleToDimension.height));
  }

  /**
   * Lädt alle Bilder aus den resource Verzeichnissen, die auf dem Classpath liegen. Die Bilder müssen dabei eine der
   * unterstützten Dateiendungen besitzen.
   */
  public void loadAllImagesFromResources() {
    loadAllImagesFromResources("", SUPPORTED_FILE_EXTENSIONS);
  }

  /**
   * Lädt alle Bilder aus den resource Verzeichnissen, die auf dem Classpath liegen. Die Bilder müssen dabei eine der
   * unterstützten Dateiendungen besitzen.
   *
   * @param directory Pfad zu einem Unterverzeichnis beginnend bei resource in dem die Bilder liegen
   */
  public void loadAllImagesFromResources(String directory) {
    loadAllImagesFromResources(directory, SUPPORTED_FILE_EXTENSIONS);
  }

  /**
   * Lädt alle Bilder mit den angegebenen Dateiendungen aus den resource Verzeichnissen, die auf dem Classpath liegen.
   *
   * @param directory Pfad zu einem Unterverzeichnis beginnend bei resource in dem die Bilder liegen
   * @param fileExtensions Liste mit zu ladenden Dateiendungen
   */
  public void loadAllImagesFromResources(String directory, String... fileExtensions) {
    Reflections reflections = new Reflections(directory, new ResourcesScanner());
    Set<String> availableImages = new HashSet<>();
    for (String fileExtension : fileExtensions) {
      availableImages.addAll(reflections.getResources(Pattern.compile(".*\\" + fileExtension)));
    }

    int counter = 0;
    for (String imagePath : availableImages) {
      String baseName = FilenameUtils.getBaseName(imagePath);
      String extension = FilenameUtils.getExtension(imagePath);
      String key = StringUtils.join(baseName, "-", extension);
      try {
        addImage(key, imagePath);
        counter++;
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    LOG.info("{} Bilder erfolgreich geladen...", counter);
  }

  /**
   * Lädt ein Bild über die angegebene URL und legt dieses unter dem angegebenen Namen im Cache ab.
   *
   * @param imageName Name eines Bildes
   * @param url URL zum Bild
   */
  public void loadImageFromUrl(String imageName, URL url) {
    try {
      this.imageCache.put(imageName, ImageIO.read(url));
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }

  /**
   * Alle Bildnamen zu denen ein Bild gefunden und in diesem Manager hinterlegt wurde. Mit diesen Bildnamen können
   * gezielt einzelne Bilder abgerufen werden.
   *
   * @return eine Menge von Bildnamen
   */
  public Set<String> getImageNames() {
    return this.imageCache.keySet();
  }

  /**
   * Löscht alle geladenen Bilder aus dem Cache.
   */
  public void clear() {
    this.imageCache.clear();
  }

  private void addImage(String imageName, String filePath) throws IOException {
    this.imageCache.put(imageName, ImageIO.read(getClass().getResourceAsStream("/" + filePath)));
  }

  private Optional<ImageIcon> getImageIcon(String imageName, Optional<Integer> scaleToWidth,
      Optional<Integer> scaleToHeight) {
    Optional<Image> image = getImage(imageName, scaleToWidth, scaleToHeight);
    Optional<ImageIcon> result = Optional.empty();
    if (image.isPresent()) {
      result = Optional.of(new ImageIcon(image.get()));
    }
    return result;
  }

  private Optional<Image> getImage(String imageName, Optional<Integer> scaleToWidth, Optional<Integer> scaleToHeight) {
    Optional<Image> result = Optional.ofNullable(this.imageCache.get(imageName));
    int width = 0;
    int height = 0;
    int newWidth = 0;
    int newHeight = 0;
    if (result.isPresent()) {
      BufferedImage img = (BufferedImage) result.get();
      width = img.getWidth();
      height = img.getHeight();
      if (scaleToWidth.isPresent()) {
        newWidth = scaleToWidth.get();
      }
      if (scaleToHeight.isPresent()) {
        newHeight = scaleToHeight.get();
      }
      if ((newWidth > 0 && newHeight > 0) && (width != newWidth || height != newHeight)) {
        result = Optional.of(result.get().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
      }
    } else {
      LOG.warn("Image with name {} is not available!", imageName);
    }
    return result;
  }
}
