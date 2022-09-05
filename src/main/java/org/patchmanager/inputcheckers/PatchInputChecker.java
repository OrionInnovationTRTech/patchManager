package org.patchmanager.inputcheckers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatchInputChecker {
  static final Logger LOGGER = LogManager.getLogger(PatchInputChecker.class);

  /**
   * Checks if the patch is in integer form
   * @param patch is user input
   * @return true if in integer form, false if not
   */
  public static boolean patchInputChecker(String patch) {
    LOGGER.debug("Checking if the patch fits the criteria");
    //Regex is in the form of number
    Pattern patternPatch = Pattern.compile("\\d+");
    Matcher matcherPatch = patternPatch.matcher(patch);
    return matcherPatch.matches();

  }
}
