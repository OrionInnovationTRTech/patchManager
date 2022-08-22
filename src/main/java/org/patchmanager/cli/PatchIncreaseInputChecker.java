package org.patchmanager.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.cli.OptionsRelated.FCCREATION;
import static org.patchmanager.cli.OptionsRelated.PATCH;

public class PatchIncreaseInputChecker {
  static final Logger LOGGER = LogManager.getLogger(PatchIncreaseInputChecker.class);

  public static void patchIncreaseInputChecker(CommandLine commandLine) {
    LOGGER.debug("Checking if the patch increase fits the criteria");
    //Regex is in the form of number
    String patchIncreaseValue = commandLine.getOptionValue(FCCREATION);
    Pattern patternPatch = Pattern.compile("\\d+");
    Matcher matcherPatch = patternPatch.matcher(patchIncreaseValue);
    if (!matcherPatch.matches()) {
      throw new IllegalArgumentException("Patch increase does not fit the criteria");
    }
    if(Integer.parseInt(patchIncreaseValue) > 198 || Integer.parseInt(patchIncreaseValue) < 0){
      throw new IllegalArgumentException("Patch increase does not fit the criteria");
    }


  }
}
