package org.patchmanager.cli;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptionsRelated {
  static final Logger LOGGER = LogManager.getLogger(OptionsRelated.class);
  public static final Option LABEL = new Option("l", "label", true, "Specify the label e.g. KL_4.8.1_P_4");
  public static final Option VERSION = new Option("v", "version", true, "Specify the version in the format of number.number.number.charcharnumber e.g. 9.8.1.dl35");
  public static final Option PATCH = new Option("p", "patch", true, "Specify the patch as a number e.g. 14");
  public static final Option MVNINSTALL = new Option("m", "mvninstall", false, "Does maven install on lab and sends the war file to zitsvy");
  public static final Option FCCREATION = new Option("fcc", "fccreation", true, "FC Creation, creates FC and Patch files," +
                                                                                                                "use it with the version option and enter the number of fixes as a value " +
                                                                                                                "i.e. -fcc 5 makes 9.8.1.dm90 to 9.8.1.dm95");
  public static final Option PATCHCREATION = new Option("pc", "patchcreation", false, "Currently used for testing");
  public static final Option TERMINAL = new Option("t", "terminal", true, "Acts like a very basic terminal, use value labUsr to connect lab 136, use any other thing to connect to zitsvy");
  public CommandLineParser commandLineParser = new DefaultParser();
  public static Options optionsList;

  public OptionsRelated() {
    LOGGER.debug("Creating optionsList");
    optionsList = new Options();
    optionsList.addOption(LABEL);
    optionsList.addOption(VERSION);
    optionsList.addOption(PATCH);
    optionsList.addOption(MVNINSTALL);
    optionsList.addOption(FCCREATION);
    optionsList.addOption(PATCHCREATION);
    optionsList.addOption(TERMINAL);
  }

}
