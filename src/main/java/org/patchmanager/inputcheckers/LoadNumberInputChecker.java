package org.patchmanager.inputcheckers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadNumberInputChecker {
  static Logger LOGGER = LogManager.getLogger(LoadNumberInputChecker.class);

  /**
   * Checks if the load number is in the form of dm64 etc.
   * @param loadNumber
   * @return false if not in correct form, true if in correct form
   */
  public static boolean loadNumberInputChecker(String loadNumber) {
    LOGGER.debug("Checking if the load number fits the criteria");
    //Regex is in the form of charchardigitdigit dm64 aa01 az11 etc.
    Pattern patternLoadNo = Pattern.compile("[a-z]{2}\\d{2}");
    Matcher matcherLoadNo = patternLoadNo.matcher(loadNumber);
    return matcherLoadNo.matches();

  }
}
