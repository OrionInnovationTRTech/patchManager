package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.command_line.OptionsRelated.*;

public class PatchInputChecker {
    public static void patchInputChecker(CommandLine commandLine){
        //Regex is in the form of number
        Pattern patternPatch = Pattern.compile("\\d+");
        Matcher matcherPatch = patternPatch.matcher(commandLine.getOptionValue(PATCH));
        if (!matcherPatch.matches()) {
            throw new IllegalArgumentException("Patch does not fit the criteria");
        }

    }
}
