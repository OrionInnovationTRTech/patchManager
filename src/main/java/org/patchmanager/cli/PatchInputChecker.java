package org.patchmanager.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.cli.OptionsRelated.PATCH;

public class PatchInputChecker {
  static final Logger LOGGER = LogManager.getLogger(PatchInputChecker.class);

  public static void patchInputChecker(CommandLine commandLine) {
    LOGGER.debug("Checking if the patch fits the criteria");
    //Regex is in the form of number
    Pattern patternPatch = Pattern.compile("\\d+");
    Matcher matcherPatch = patternPatch.matcher(commandLine.getOptionValue(PATCH));
    if (!matcherPatch.matches()) {
      throw new IllegalArgumentException("Patch does not fit the criteria");
    }

  }
}
