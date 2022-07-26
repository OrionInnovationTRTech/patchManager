package org.patchmanager.command_line;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;

public class PrintHelpCmd {
    static final Logger LOGGER  = LogManager.getLogger(PrintHelpCmd.class);
    public static void printHelpCmd(Options options){
        LOGGER.debug("Creating a HelpFormatter");
        HelpFormatter formatter = new HelpFormatter();
        LOGGER.debug("Creating a PrintWriter");
        PrintWriter pw = new PrintWriter(System.out);
        LOGGER.debug("Printing usage and options a PrintWriter");
        pw.println("PackageManager");

        formatter.printUsage(pw, 100, "java -jar patchManager.jar -l label -v version -p patchNo");
        formatter.printOptions(pw, 100,options,2,5);
        pw.close();
    }
}
