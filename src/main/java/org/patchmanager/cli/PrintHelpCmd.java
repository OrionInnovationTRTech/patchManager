package org.patchmanager.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.Writer;

public class PrintHelpCmd {
  static final Logger LOGGER = LogManager.getLogger(PrintHelpCmd.class);

  public static void printHelpCmd(Options options) {
    LOGGER.debug("Creating a HelpFormatter");
    HelpFormatter formatter = new HelpFormatter();
    LOGGER.debug("Creating a PrintWriter");
    PrintWriter pw = new PrintWriter(System.out);
    LOGGER.debug("Printing usage and options with a PrintWriter");
    pw.println("PackageManager");

    formatter.printUsage(pw, 100, "For writing jira issues to file: java -jar patchManager.jar -l label -v version -p patchNo");
    formatter.printUsage(pw, 100, "For getting fc and patch files in zitsvy: java -jar patchManager.jar -v version -fc increaseInPatch");
    formatter.printOptions(pw, 100, options, 2, 5);
    pw.close();
  }
}
