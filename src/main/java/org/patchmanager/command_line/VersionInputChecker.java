package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.command_line.OptionsRelated.VERSION;

public class VersionInputChecker {
    static final Logger LOGGER  = LogManager.getLogger(VersionInputChecker.class);
    public static void versionInputChecker(CommandLine commandLine){
        LOGGER.debug("Checking if the version fits the criteria");
        //Regex is in the form of number . number . number . twoChars number
        Pattern patternVersion = Pattern.compile("\\d+.\\d+.\\d+.[a-zA-Z]{2}\\d{2}");
        Matcher matcherVersion = patternVersion.matcher(commandLine.getOptionValue(VERSION));

        if (!matcherVersion.matches()) {
            throw new IllegalArgumentException("Version does not fit the criteria");
        }

    }
}
