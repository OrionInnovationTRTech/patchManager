package org.patchmanager.inputcheckers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VersionInputChecker {
  static final Logger LOGGER = LogManager.getLogger(VersionInputChecker.class);

  /**
   * Returns false if the version isn't in the correct form which is number . number . number . twoChars number
   * @param version
   * @return true if correct form false if not in correct form
   */
  public static boolean versionInputChecker(String version) {
    LOGGER.debug("Checking if the version fits the criteria");
    Pattern patternVersion = Pattern.compile("\\d+.\\d+.\\d+.[a-zA-Z]{2}\\d{2}");
    Matcher matcherVersion = patternVersion.matcher(version);

    if (!matcherVersion.matches()) {
      return false;
    }
    return true;

  }
}
