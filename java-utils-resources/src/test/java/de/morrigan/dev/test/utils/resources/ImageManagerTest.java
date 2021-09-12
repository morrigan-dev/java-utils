package de.morrigan.dev.test.utils.resources;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import de.morrigan.dev.utils.resources.ImageManager;

public class ImageManagerTest {

  private ImageManager sut = ImageManager.getInstance();

  @Before
  public void setup() {
    this.sut.clear();
  }

  @After
  public void tearDown() {
    this.sut.clear();
  }

  @Test
  public void testLoadAllImagesFromResources() {
    this.sut.loadAllImagesFromResources();

    Optional<Image> greenImage = this.sut.getImage("20x20_green-bmp");
    assertThat(greenImage, is(optionalWithValue()));
    Color colorGreen = new Color(((BufferedImage) greenImage.get()).getRGB(10, 10));
    assertThat(colorGreen.getGreen(), is(equalTo(255)));

    Optional<Image> redImage = this.sut.getImage("20x20_red-bmp");
    assertThat(redImage, is(optionalWithValue()));
    Color colorRed = new Color(((BufferedImage) redImage.get()).getRGB(10, 10));
    assertThat(colorRed.getRed(), is(equalTo(255)));

    assertThat(this.sut.getImageNames(), hasSize(15));
    assertThat(this.sut.getImageNames(),
        containsInAnyOrder("20x20_green-bmp", "20x20_green-gif", "20x20_green-ico", "20x20_green-jpeg",
            "20x20_green-jpg", "20x20_green-png", "20x20_green-tif", "20x20_green-tiff", "20x20_red-bmp",
            "20x20_red-gif", "20x20_red-ico", "20x20_red-jpg", "20x20_red-png", "20x20_red-tif", "97461-png"));
  }

  @Test
  public void testLoadAllImagesFromResourcesInImagesDir() {
    this.sut.loadAllImagesFromResources("images");

    Optional<Image> greenImage = this.sut.getImage("20x20_green-bmp");
    assertThat(greenImage, is(optionalWithValue()));
    Color colorGreen = new Color(((BufferedImage) greenImage.get()).getRGB(10, 10));
    assertThat(colorGreen.getGreen(), is(equalTo(255)));

    Optional<Image> redImage = this.sut.getImage("20x20_red-bmp");
    assertThat(redImage, is(optionalWithValue()));
    Color colorRed = new Color(((BufferedImage) redImage.get()).getRGB(10, 10));
    assertThat(colorRed.getRed(), is(equalTo(255)));

    assertThat(this.sut.getImageNames(), hasSize(15));
    assertThat(this.sut.getImageNames(),
        containsInAnyOrder("20x20_green-bmp", "20x20_green-gif", "20x20_green-ico", "20x20_green-jpeg",
            "20x20_green-jpg", "20x20_green-png", "20x20_green-tif", "20x20_green-tiff", "20x20_red-bmp",
            "20x20_red-gif", "20x20_red-ico", "20x20_red-jpg", "20x20_red-png", "20x20_red-tif", "97461-png"));
  }

  @Test
  public void testLoadAllImagesFromResourcesInSpecialDir() {
    this.sut.loadAllImagesFromResources("images/red");

    Optional<Image> redImage = this.sut.getImage("20x20_red-bmp");
    assertThat(redImage, is(optionalWithValue()));
    Color colorRed = new Color(((BufferedImage) redImage.get()).getRGB(10, 10));
    assertThat(colorRed.getRed(), is(equalTo(255)));

    assertThat(this.sut.getImageNames(), hasSize(6));
    assertThat(this.sut.getImageNames(),
        containsInAnyOrder("20x20_red-bmp", "20x20_red-gif", "20x20_red-ico", "20x20_red-jpg", "20x20_red-png",
            "20x20_red-tif"));
  }

  @Test
  public void testLoadAllImagesFromResourceInUpperDir() {
    this.sut.loadAllImagesFromResources("images/upper");

    assertThat(this.sut.getImageNames(), hasSize(6));
    assertThat(this.sut.getImageNames(),
        containsInAnyOrder("20x20_green-bmp", "20x20_green-gif", "20x20_green-ico", "20x20_green-jpg",
            "20x20_green-png", "20x20_green-tif"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithBmpExtension() {
    this.sut.loadAllImagesFromResources("images", ".bmp");
    assertThat(this.sut.getImageNames(), hasSize(2));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-bmp", "20x20_green-bmp"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithGifExtension() {
    this.sut.loadAllImagesFromResources("images", ".gif");
    assertThat(this.sut.getImageNames(), hasSize(2));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-gif", "20x20_green-gif"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithIcoExtension() {
    this.sut.loadAllImagesFromResources("images", ".ico");
    assertThat(this.sut.getImageNames(), hasSize(2));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-ico", "20x20_green-ico"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithJpgExtension() {
    this.sut.loadAllImagesFromResources("images", ".jpg");
    assertThat(this.sut.getImageNames(), hasSize(2));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-jpg", "20x20_green-jpg"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithPngExtension() {
    this.sut.loadAllImagesFromResources("images", ".png");
    assertThat(this.sut.getImageNames(), hasSize(3));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-png", "20x20_green-png", "97461-png"));
  }

  @Test
  public void testLoadAllImagesFromResourcesWithTifExtension() {
    this.sut.loadAllImagesFromResources("images", ".tif");
    assertThat(this.sut.getImageNames(), hasSize(2));
    assertThat(this.sut.getImageNames(), containsInAnyOrder("20x20_red-tif", "20x20_green-tif"));
  }

  @Test
  public void testLoadImageFromUrl() throws IOException {
    URL url = new URL("https://render.guildwars2.com/file/25B230711176AB5728E86F5FC5F0BFAE48B32F6E/97461.png");
    this.sut.loadImageFromUrl("97461-png", url);
    Optional<Image> image = this.sut.getImage("97461-png");
    assertThat(image, is(not(emptyOptional())));
    BufferedImage actualImage = (BufferedImage) image.get();
    BufferedImage expectedImage = ImageIO.read(getClass().getResourceAsStream("/images/97461.png"));
    ImageComparisonResult result = new ImageComparison(expectedImage, actualImage).compareImages();
    assertEquals(ImageComparisonState.MATCH, result.getImageComparisonState());
  }

  @Test
  public void testLoadImageFromMissingUrl() throws IOException {
    URL url = new URL("https://render.guildwars2.com/file/25B230711176AB5728E86F5FC5F0BFAE48B32F6E/9746.png");
    this.sut.loadImageFromUrl("9746-png", url);
    Optional<Image> image = this.sut.getImage("9746-png");
    assertThat(image, is(emptyOptional()));
    assertThat(this.sut.getImageNames(), hasSize(0));
  }

  @Test
  public void testGetImageWithMissingImage() {
    Optional<Image> image = this.sut.getImage("missingimage");
    assertThat(image, is(emptyOptional()));
  }

  @Test
  public void testGetImageWithDimension() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<Image> image = this.sut.getImage("20x20_red-png", new Dimension(10, 10));
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getWidth(null), is(equalTo(10)));
    assertThat((image.get()).getHeight(null), is(equalTo(10)));
  }

  @Test
  public void testGetImageWithScaleWidth() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<Image> image = this.sut.getImage("20x20_red-png", 10, 20);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getWidth(null), is(equalTo(10)));
    assertThat((image.get()).getHeight(null), is(equalTo(20)));
  }

  @Test
  public void testGetImageWithScaleHeight() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<Image> image = this.sut.getImage("20x20_red-png", 20, 10);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getWidth(null), is(equalTo(20)));
    assertThat((image.get()).getHeight(null), is(equalTo(10)));
  }

  @Test
  public void testGetImageWithScaleWidthAndHeight() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<Image> image = this.sut.getImage("20x20_red-png", 10, 10);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getWidth(null), is(equalTo(10)));
    assertThat((image.get()).getHeight(null), is(equalTo(10)));
  }

  @Test
  public void testGetImageIconWithMissingImage() {
    Optional<ImageIcon> image = this.sut.getImageIcon("missingimage");
    assertThat(image, is(emptyOptional()));
  }

  @Test
  public void testGetImageIconWithDimension() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<ImageIcon> image = this.sut.getImageIcon("20x20_red-png", new Dimension(10, 10));
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getIconWidth(), is(equalTo(10)));
    assertThat((image.get()).getIconHeight(), is(equalTo(10)));
  }

  @Test
  public void testGetImageIconWithScaleWidth() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<ImageIcon> image = this.sut.getImageIcon("20x20_red-png", 10, 20);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getIconWidth(), is(equalTo(10)));
    assertThat((image.get()).getIconHeight(), is(equalTo(20)));
  }

  @Test
  public void testGetImageIconWithScaleHeight() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<ImageIcon> image = this.sut.getImageIcon("20x20_red-png", 20, 10);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getIconWidth(), is(equalTo(20)));
    assertThat((image.get()).getIconHeight(), is(equalTo(10)));
  }

  @Test
  public void testGetImageIconWithScaleWidthAndHeight() {
    this.sut.loadAllImagesFromResources("images/red");
    Optional<ImageIcon> image = this.sut.getImageIcon("20x20_red-png", 10, 10);
    assertThat(image, is(optionalWithValue()));
    assertThat((image.get()).getIconWidth(), is(equalTo(10)));
    assertThat((image.get()).getIconHeight(), is(equalTo(10)));
  }

  @Test
  public void testClear() {
    this.sut.loadAllImagesFromResources("images");
    assertThat(this.sut.getImageNames(), is(not(empty())));
    this.sut.clear();
    assertThat(this.sut.getImageNames(), is(empty()));
  }
}
