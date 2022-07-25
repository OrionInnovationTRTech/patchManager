package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.command_line.OptionsRelated.VERSION;

public class VersionInputChecker {
    public static void versionInputChecker(CommandLine commandLine){
        //Regex is in the form of number . number . number . twoChars number
        Pattern patternVersion = Pattern.compile("\\d+.\\d+.\\d+.[a-zA-Z]{2}\\d{2}");
        Matcher matcherVersion = patternVersion.matcher(commandLine.getOptionValue(VERSION));

        if (!matcherVersion.matches()) {
            throw new IllegalArgumentException("Version does not fit the criteria");
        }

    }
}
