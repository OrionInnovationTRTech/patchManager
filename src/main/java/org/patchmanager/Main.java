package org.patchmanager;

import org.apache.commons.cli.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.api_related.DotEnvUser;
import org.patchmanager.api_related.HttpRequestAndResponse;
import org.patchmanager.command_line.OptionsRelated;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.patchmanager.api_related.AuthChecker.checkAuth;
import static org.patchmanager.api_related.ParseJiraIssues.parseJiraIssues;
import static org.patchmanager.command_line.MissingOptionChecker.missingOptionChecker;
import static org.patchmanager.command_line.PatchInputChecker.patchInputChecker;
import static org.patchmanager.command_line.PrintHelpCmd.printHelpCmd;
import static org.patchmanager.command_line.OptionsRelated.*;
import static org.patchmanager.command_line.VersionInputChecker.versionInputChecker;
import static org.patchmanager.writing_to_file.DecideFileName.fileNameDecider;
import static org.patchmanager.writing_to_file.WriteIntro.writeIntro;
import static org.patchmanager.writing_to_file.WriteOutro.writeOutro;
import static org.patchmanager.writing_to_file.WriteToFile.writeToFile;


public class Main {
    static final Logger LOGGER  = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.debug("Started the main function");
        String labelInput = "";
        String versionInput = "";
        String patchInput = "";
        OptionsRelated optionsRelatedObj = new OptionsRelated();

        try {
            CommandLine commandLine = null;
            try {
                LOGGER.info("Parsing commandline");
                commandLine = optionsRelatedObj.commandLineParser.parse(optionsList, args);
            } catch (ParseException e) {
                LOGGER.fatal(e.getMessage());
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            try {
                LOGGER.info("Checking if there is a missing option");
                missingOptionChecker(commandLine);
            } catch (Exception e) {
                LOGGER.fatal("There is a missing option");
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            labelInput = commandLine.getOptionValue(LABEL);

            try {
                LOGGER.info("Checking if the version is in correct form");
                versionInputChecker(commandLine);
            } catch (Exception e) {
                LOGGER.fatal("Version does not fit the criteria");
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            versionInput = commandLine.getOptionValue(VERSION);


            try {
                LOGGER.info("Checking if the patch is in correct form");
                patchInputChecker(commandLine);
            } catch (Exception e) {
                LOGGER.fatal("Patch does not fit the criteria");
                printHelpCmd(optionsList);
                System.exit(-1);
            }
            patchInput = commandLine.getOptionValue(PATCH);
        } catch (Exception e) {
            LOGGER.fatal(e.getMessage());
        }
        LOGGER.info("Trying to authorize");
        DotEnvUser dotEnvUserObj = new DotEnvUser();


        if (!checkAuth(dotEnvUserObj.email, dotEnvUserObj.api)) {
            LOGGER.fatal("Couldn't authorize, check credentials");
            System.exit(-1);
        }

        LOGGER.info("Defining version values");
        //There are 2 versions that are used, a higher one e.g. 9.8.1 and a lower one e.g. 4.8.1
        //Split by numbers
        String[] splitVersionByNumbers = versionInput.split("(\\D+)");
        String versionHigher = String.join(".", splitVersionByNumbers[0], splitVersionByNumbers[1], splitVersionByNumbers[2]);
        String lowerFirstNoByFive = String.valueOf(Integer.parseInt(splitVersionByNumbers[0]) - 5);
        String versionLower = String.join(".", lowerFirstNoByFive, splitVersionByNumbers[1], splitVersionByNumbers[2]);

        LOGGER.info("Creating a variable for today's date");
        Date date = new Date();
        SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat("yyyyMMdd");
        String strDate = simpleDateFormatObj.format(date);

        LOGGER.info("Deciding on filename");
        String fileName = fileNameDecider(patchInput, versionInput);

        LOGGER.info("Trying to send a Http Request to the API and get a response");
        HttpRequestAndResponse httpRequestAndResponse = new HttpRequestAndResponse(labelInput);

        if (!parseJiraIssues(httpRequestAndResponse.response.body()).isEmpty()) {
            StringBuilder finalStr = new StringBuilder();
            finalStr.append(writeIntro(patchInput, versionLower, versionHigher, versionInput, strDate))
                    .append(parseJiraIssues(httpRequestAndResponse.response.body()))
                    .append(writeOutro(patchInput, versionHigher));
            writeToFile(finalStr.toString(), fileName);
        } else {
            LOGGER.fatal("No issues found with that label");
            System.exit(-1);
        }
        LOGGER.info("Finishing the main function");
    }


    //+try catch null option missing
    //mockito
    //api test
    //+change name of classes to capital starting letter
    //+display path and success
    //+two chars
    //remove [] things and such
    //log4j



}