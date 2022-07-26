package org.patchmanager.command_line;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.api_related.HttpRequestAndResponse;

import static org.patchmanager.command_line.OptionsRelated.*;

public class MissingOptionChecker {
    static final Logger LOGGER  = LogManager.getLogger(MissingOptionChecker.class);
    public static void missingOptionChecker(CommandLine commandLine) throws MissingArgumentException {
        LOGGER.debug("Checking if there is a missing option");
        if (!commandLine.hasOption(PATCH.getOpt()) || !commandLine.hasOption(VERSION.getOpt()) || !commandLine.hasOption(LABEL.getOpt())) {
            throw new MissingArgumentException("There is a missing option");
        }

    }
}
