package org.patchmanager.apiutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;

public class EncodeBase64 {
  static final Logger LOGGER = LogManager.getLogger(EncodeBase64.class);

  public static String encodeBase64(String email, String api) {
    LOGGER.debug("Encoding the email and api");
    String wantedPlainString = email + ":" + api;
    String encodedString = Base64.getEncoder().encodeToString(wantedPlainString.getBytes());
    return "Basic " + encodedString;
  }
}
