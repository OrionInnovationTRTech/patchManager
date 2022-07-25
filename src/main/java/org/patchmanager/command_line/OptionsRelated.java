package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsRelated {
    public static final Option LABEL = new Option("l", "label", true, "Specify the label e.g. KL_4.8.1_P_4");
    public static final Option VERSION = new Option("v", "version", true, "Specify the version in the format of number.number.number.charcharnumber e.g. 9.8.1.dl35" );
    public static final Option PATCH = new Option("p", "patch", true, "Specify the patch as a number e.g. 14");

    public CommandLineParser commandLineParser = new DefaultParser();
    public static Options optionsList;

    public OptionsRelated() {
        optionsList = new Options();
        optionsList.addOption(LABEL);
        optionsList.addOption(VERSION);
        optionsList.addOption(PATCH);
    }

}
