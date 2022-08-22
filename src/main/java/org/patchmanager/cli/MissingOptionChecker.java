package org.patchmanager.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.patchmanager.cli.OptionsRelated.*;

public class MissingOptionChecker {
  static final Logger LOGGER = LogManager.getLogger(MissingOptionChecker.class);

  public static String missingOptionChecker(CommandLine commandLine, String switchValue) throws MissingArgumentException {
    LOGGER.debug("Checking if there is a missing option");
    if  (commandLine.hasOption(PATCH.getOpt()) && commandLine.hasOption(VERSION.getOpt())
        && commandLine.hasOption(LABEL.getOpt())) {
      switchValue = "JiraIssueToText";
    } else if (commandLine.hasOption(TERMINAL.getOpt())) {
      switchValue = "TERMINAL";
    } else if (commandLine.hasOption(FCCREATION.getOpt()) && commandLine.hasOption(VERSION.getOpt())) {
      switchValue = "FCCREATION";
    } else if (commandLine.hasOption(MVNINSTALL.getOpt())) {
      switchValue = "MVNINSTALL";
    } else if (commandLine.hasOption(PATCHCREATION.getOpt())) {
      switchValue = "PATCHCREATION";
    } else {
      throw new MissingArgumentException("There is a missing option");
    }
    return switchValue;
  }
}
