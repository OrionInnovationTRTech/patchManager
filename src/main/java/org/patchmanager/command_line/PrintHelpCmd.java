package org.patchmanager.command_line;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.PrintWriter;

public class PrintHelpCmd {
    public static void printHelpCmd(Options options){
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("PackageManager");

        formatter.printUsage(pw, 100, "java -jar patchManager.jar -l label -v version -p patchNo");
        formatter.printOptions(pw, 100,options,2,5);
        pw.close();
    }
}
