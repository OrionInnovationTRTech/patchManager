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
        LOGGER.debug("It is a debug logger.");
        LOGGER.error("It is an error logger.");
        LOGGER.fatal("It is a fatal logger.");
        LOGGER.info("It is a info logger.");
        LOGGER.trace("It is a trace logger.");
        LOGGER.warn("It is a warn logger.");
        LOGGER.info("Started the main function");
        String labelInput = "";
        String versionInput = "";
        String patchInput = "";
        OptionsRelated optionsRelatedObj = new OptionsRelated();

        try {
            CommandLine commandLine = null;
            try {
                commandLine = optionsRelatedObj.commandLineParser.parse(optionsList, args);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            try {
                missingOptionChecker(commandLine);
            } catch (Exception e) {
                System.out.println("There is a missing option");
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            labelInput = commandLine.getOptionValue(LABEL);

            try {
                versionInputChecker(commandLine);
            } catch (Exception e) {
                System.out.println("Version does not fit the criteria");
                printHelpCmd(optionsList);
                System.exit(-1);
            }

            versionInput = commandLine.getOptionValue(VERSION);


            try {
                patchInputChecker(commandLine);
            } catch (Exception e) {
                System.out.println("Patch does not fit the criteria");
                printHelpCmd(optionsList);
                System.exit(-1);
            }
            patchInput = commandLine.getOptionValue(PATCH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DotEnvUser dotEnvUserObj = new DotEnvUser();

        if (!checkAuth(dotEnvUserObj.email, dotEnvUserObj.api)) {
            System.out.println("Couldn't authorize, check credentials");
            System.exit(-1);
        }

        //There are 2 versions that are used, a higher one e.g. 9.8.1 and a lower one e.g. 4.8.1
        //Split by numbers
        String[] splitVersionByNumbers = versionInput.split("(\\D+)");
        String versionHigher = String.join(".", splitVersionByNumbers[0], splitVersionByNumbers[1], splitVersionByNumbers[2]);
        String lowerFirstNoByFive = String.valueOf(Integer.parseInt(splitVersionByNumbers[0]) - 5);
        String versionLower = String.join(".", lowerFirstNoByFive, splitVersionByNumbers[1], splitVersionByNumbers[2]);

        Date date = new Date();
        SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat("yyyyMMdd");
        String strDate = simpleDateFormatObj.format(date);

        String fileName = fileNameDecider(patchInput, versionInput);

        HttpRequestAndResponse httpRequestAndResponse = new HttpRequestAndResponse(labelInput);

        if (!parseJiraIssues(httpRequestAndResponse.response.body()).isEmpty()) {
            StringBuilder finalStr = new StringBuilder();
            finalStr.append(writeIntro(patchInput, versionLower, versionHigher, versionInput, strDate))
                    .append(parseJiraIssues(httpRequestAndResponse.response.body()))
                    .append(writeOutro(patchInput, versionHigher));
            writeToFile(finalStr.toString(), fileName);
        } else {
            System.out.println("No issues found with that label");
            System.exit(-1);
        }

    }


    //try catch null option missing
    //mockito
    //api test
    //+change name of classes to capital starting letter
    //+display path and success
    //+two chars
    //remove [] things and such
    //log4j



}