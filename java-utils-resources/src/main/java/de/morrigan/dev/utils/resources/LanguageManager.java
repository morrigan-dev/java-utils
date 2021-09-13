package de.morrigan.dev.utils.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageManager {

  /** Logger für Debug/Fehlerausgaben */
  private static final Logger LOG = LoggerFactory.getLogger(LanguageManager.class);

  public static LanguageManager getInstance() {
    return INSTANCE;
  }

  private static final LanguageManager INSTANCE = new LanguageManager();

}
