package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;

import static org.patchmanager.command_line.OptionsRelated.*;

public class MissingOptionChecker {
    public static void missingOptionChecker(CommandLine commandLine) throws MissingArgumentException {
        if (!commandLine.hasOption(PATCH.getOpt()) || !commandLine.hasOption(VERSION.getOpt()) || !commandLine.hasOption(LABEL.getOpt())) {
            throw new MissingArgumentException("There is a missing option");
        }

    }
}
