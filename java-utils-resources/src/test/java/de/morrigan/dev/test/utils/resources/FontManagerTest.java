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

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.Optional;

import org.junit.Test;

import de.morrigan.dev.utils.resources.FontManager;

public class FontManagerTest {

  public FontManager sut = FontManager.getInstance();

  @Test
  public void testLoadAllFontsFromResources() {
    sut.clear();
    sut.loadAllFontsFromResources();

    Optional<Font> cronosRegular = sut.getFont("cronos-pro-regular");
    assertThat(cronosRegular, is(optionalWithValue()));
    assertThat(cronosRegular.get().getFontName(), is(equalTo("CronosPro-Regular")));

    Optional<Font> cronosItalic = sut.getFont("cronos-pro-italic");
    assertThat(cronosItalic, is(optionalWithValue()));
    assertThat(cronosItalic.get().getFontName(), is(equalTo("CronosPro-Italic")));

    assertThat(sut.getFontKeys(), hasSize(4));
    assertThat(sut.getFontKeys(),
        containsInAnyOrder("cronos-pro-regular", "cronos-pro-italic", "menomonia", "menomonia-italic"));
  }

  @Test
  public void testLoadAllFontsFromResourcesInFontDir() {
    sut.clear();
    sut.loadAllFontsFromResources("font");

    Optional<Font> cronosRegular = sut.getFont("cronos-pro-regular");
    assertThat(cronosRegular, is(optionalWithValue()));
    assertThat(cronosRegular.get().getFontName(), is(equalTo("CronosPro-Regular")));

    Optional<Font> cronosItalic = sut.getFont("cronos-pro-italic");
    assertThat(cronosItalic, is(optionalWithValue()));
    assertThat(cronosItalic.get().getFontName(), is(equalTo("CronosPro-Italic")));

    assertThat(sut.getFontKeys(), hasSize(4));
    assertThat(sut.getFontKeys(),
        containsInAnyOrder("cronos-pro-regular", "cronos-pro-italic", "menomonia", "menomonia-italic"));
  }

  @Test
  public void testLoadAllFontsFromResourcesInSpecialDir() {
    sut.clear();
    sut.loadAllFontsFromResources("font/special");

    Optional<Font> menomonia = sut.getFont("menomonia");
    assertThat(menomonia, is(optionalWithValue()));

    Optional<Font> menomoniaItalic = sut.getFont("menomonia-italic");
    assertThat(menomoniaItalic, is(optionalWithValue()));

    assertThat(sut.getFontKeys(), hasSize(2));
    assertThat(sut.getFontKeys(), containsInAnyOrder("menomonia", "menomonia-italic"));
  }

  @Test
  public void testGetFontWithMissingFont() {
    Optional<Font> font = sut.getFont("missingfont");
    assertThat(font, is(emptyOptional()));
  }

  @Test
  public void testGetFontWithGivenSize() {
    sut.loadAllFontsFromResources("font/special");
    Optional<Font> font = sut.getFont("menomonia", 26f);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().getSize(), is(equalTo(26)));
  }

  @Test
  public void testGetFontWithGivenStyle() {
    sut.loadAllFontsFromResources("font/special");
    Optional<Font> font = sut.getFont("menomonia", Font.BOLD);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().isBold(), is(equalTo(true)));
  }

  @Test
  public void testGetFontWithGivenTransformation() {
    sut.loadAllFontsFromResources("font/special");
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(Math.PI / 2);
    Optional<Font> font = sut.getFont("menomonia", affineTransform);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().getTransform(), is(equalTo(affineTransform)));
  }

  @Test
  public void testGetFontWithGivenSizeAndStyle() {
    sut.loadAllFontsFromResources("font/special");
    Optional<Font> font = sut.getFont("menomonia", 26f, Font.BOLD);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().getSize(), is(equalTo(26)));
    assertThat(font.get().isBold(), is(equalTo(true)));
  }

  @Test
  public void testGetFontWithGivenStyleAndTransformation() {
    sut.loadAllFontsFromResources("font/special");
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(Math.PI / 2);
    Optional<Font> font = sut.getFont("menomonia", Font.BOLD, affineTransform);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().isBold(), is(equalTo(true)));
    assertThat(font.get().getTransform(), is(equalTo(affineTransform)));
  }

  @Test
  public void testGetFontWithGivenSizeAndTransformation() {
    sut.loadAllFontsFromResources("font/special");
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(Math.PI / 2);
    Optional<Font> font = sut.getFont("menomonia", 26f, affineTransform);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().getSize(), is(equalTo(26)));
    assertThat(font.get().getTransform(), is(equalTo(affineTransform)));
  }

  @Test
  public void testGetFontWithGivenSizeAndStyleAndTransformation() {
    sut.loadAllFontsFromResources("font/special");
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(Math.PI / 2);
    Optional<Font> font = sut.getFont("menomonia", 26f, Font.BOLD, affineTransform);
    assertThat(font, is(optionalWithValue()));
    assertThat(font.get().getSize(), is(equalTo(26)));
    assertThat(font.get().isBold(), is(equalTo(true)));
    assertThat(font.get().getTransform(), is(equalTo(affineTransform)));
  }

  @Test
  public void testClear() {
    sut.loadAllFontsFromResources("font");
    assertThat(sut.getFontKeys(), is(not(empty())));
    sut.clear();
    assertThat(sut.getFontKeys(), is(empty()));
  }
}
