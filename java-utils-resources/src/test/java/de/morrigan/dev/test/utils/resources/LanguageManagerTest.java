package de.morrigan.dev.test.utils.resources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.morrigan.dev.utils.resources.LanguageManager;

public class LanguageManagerTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private LanguageManager sut = LanguageManager.getInstance();

  @Before
  public void setup() {
    ResourceBundle.clearCache();
    Locale.setDefault(Locale.GERMANY);
  }

  @Test
  public void testLoadLabelsFromResource() {
    this.sut.loadLabelsFromResources("language/lang");
    assertThat(this.sut.getLabelKeys(), hasSize(1));
    assertThat(this.sut.getLabel("helloWorld"), is(equalTo("Hello World!")));
  }

  @Test
  public void testLoadLabelsFromResourceWithLocalGerman() {
    this.sut.loadLabelsFromResources("language/labels", Locale.GERMANY);
    assertThat(this.sut.getLabelKeys(), hasSize(1));
    assertThat(this.sut.getLabel("helloWorld"), is(equalTo("Hallo Welt!")));
  }

  @Test
  public void testLoadLabelsFromResourceWithMissingBaseName() {
    MissingResourceException exception = assertThrows(MissingResourceException.class, () -> {
      this.sut.loadLabelsFromResources("language/missing", Locale.GERMANY);
    });
    assertThat(exception.getMessage(), containsString("language/missing"));
  }

  @Test
  public void testGetLabelWithExistingKey() {
    this.sut.loadLabelsFromResources("language/labels");
    assertThat(this.sut.getLabel("helloWorld"), is(equalTo("Hallo Welt!")));
  }

  @Test
  public void testGetLabelWithExistingKeyAndLocaleFR() {
    this.sut.loadLabelsFromResources("language/labels");
    this.sut.loadLabelsFromResources("language/labels", Locale.FRANCE);
    assertThat(this.sut.getLabel("helloWorld", Locale.FRANCE), is(equalTo("Bonjour le monde!")));
  }

  @Test
  public void testGetLabelWithMissedLoadingLocaleFR() {
    this.sut.loadLabelsFromResources("language/labels");
    assertThat(this.sut.getLabel("helloWorld", Locale.FRANCE), is(equalTo("")));
    // TODO Logger warning prüfen.
    // by morrigan on 14.09.2021
  }

  @Test
  public void testGetLabelWithNotExistingKey() {
    this.sut.loadLabelsFromResources("language/labels");
    assertThat(this.sut.getLabel("hello"), is(equalTo("")));
    // TODO Logger warning prüfen.
    // by morrigan on 14.09.2021
  }

}
