package org.patchmanager.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionBaseInputChecker {
  static final Logger LOGGER = LogManager.getLogger(VersionInputChecker.class);

  public static boolean versionBaseInputChecker(String baseVersion) {
    LOGGER.debug("Checking if the base version fits the criteria");
    Pattern patternVersion = Pattern.compile("\\d+.\\d+.\\d+");
    Matcher matcherVersion = patternVersion.matcher(baseVersion);

    if (!matcherVersion.matches()) {
      return false;
    }
    return true;

  }
}
