package org.patchmanager.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.patchmanager.cli.OptionsRelated.*;

public class MissingOptionChecker {
  static final Logger LOGGER = LogManager.getLogger(MissingOptionChecker.class);

  public static void missingOptionChecker(CommandLine commandLine) throws MissingArgumentException {
    LOGGER.debug("Checking if there is a missing option");
    // if one of p v l is empty and if there is no other option
    if ( (!commandLine.hasOption(PATCH.getOpt())
        || !commandLine.hasOption(VERSION.getOpt())
        || !commandLine.hasOption(LABEL.getOpt()))
        && !commandLine.hasOption(MVNINSTALL.getOpt())
        && !commandLine.hasOption(FCCREATION.getOpt())
        && !commandLine.hasOption(PATCHCREATION.getOpt())) {
      throw new MissingArgumentException("There is a missing option");
    }

  }
}
