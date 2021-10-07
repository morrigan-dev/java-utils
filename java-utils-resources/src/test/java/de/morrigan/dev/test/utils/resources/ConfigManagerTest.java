package de.morrigan.dev.test.utils.resources;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.morrigan.dev.utils.resources.ConfigManager;

public class ConfigManagerTest {

   private ConfigManager sut = ConfigManager.getInstance();

   @Before
   public void setup() {
      this.sut.clear();
   }

   @After
   public void tearDown() {
      this.sut.clear();
   }

   @Test
   public void testLoadConfigs() throws IOException {
      this.sut.loadAllConfigsFromResources("config.properties");
      assertThat(this.sut.getConfigKeys(), hasSize(1));
   }

   @Test
   public void testGetOptConfigWithMissingKey() {
      Optional<String> config = this.sut.getOptConfig("missing");
      assertThat(config, is(emptyOptional()));
   }

   @Test
   public void testGetOptConfigWithExistingKey() throws IOException {
      this.sut.loadAllConfigsFromResources("config.properties");
      Optional<String> config = this.sut.getOptConfig("serverMode");
      assertThat(config, is(optionalWithValue()));
      assertThat(config.get(), is(equalTo("local")));
   }

   @Test
   public void testGetConfigWithMissingKey() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.sut.getConfig("missing"));
      assertThat(exception.getMessage(), containsString("missing"));
      assertThat(exception.getMessage(), containsString("is not present"));
      assertThat(exception.getMessage(), containsString("config.properties"));
   }

   @Test
   public void testGetConfigWithExistingKey() throws IOException {
      this.sut.loadAllConfigsFromResources("config.properties");
      String config = this.sut.getConfig("serverMode");
      assertThat(config, is(equalTo("local")));
   }

   @Test
   public void testClear() throws IOException {
      this.sut.loadAllConfigsFromResources("config.properties");
      assertThat(this.sut.getConfigKeys(), hasSize(1));
      this.sut.clear();
      assertThat(this.sut.getConfigKeys(), hasSize(0));
   }

}
