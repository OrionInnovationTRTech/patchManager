package org.patchmanager.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.apiutils.HttpRequestAndResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.patchmanager.apiutils.AuthChecker.checkAuth;
import static org.patchmanager.apiutils.ParseJiraIssues.parseJiraIssues;
import static org.patchmanager.inputcheckers.PatchInputChecker.patchInputChecker;
import static org.patchmanager.inputcheckers.VersionInputChecker.versionInputChecker;
import static org.patchmanager.io.DecideFileName.fileNameDecider;
import static org.patchmanager.io.WriteIntro.writeIntro;
import static org.patchmanager.io.WriteOutro.writeOutro;
import static org.patchmanager.io.WriteToFile.writeToFile;

public class PrintJiraIssuesToTxtFile {
  private PrintJiraIssuesToTxtFile(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(PrintJiraIssuesToTxtFile.class);
  public static void printJiraIssuesToTxtFile(){

    Scanner scanner = new Scanner(System.in);
    String labelInput = "";
    String versionInput = "";
    String patchInput = "";
    System.out.print("Enter the label: ");
    labelInput = scanner.nextLine();

    System.out.print("Enter the version in the form of 9.8.1.dm10: ");
    versionInput = scanner.nextLine();
    while(!versionInputChecker(versionInput)){
      System.out.print("Wrong version format, enter again: ");
      versionInput = scanner.nextLine();
    }

    System.out.print("Enter the patch in the form of an integer: ");
    patchInput = scanner.nextLine();
    while(!patchInputChecker(patchInput)){
      System.out.print("Wrong patch format, enter again: ");
      patchInput = scanner.nextLine();
    }

    LOGGER.info("Jira Issue to Text Service Starting");
    LOGGER.info("Trying to authorize");
    new DotEnvUser();


    if (!checkAuth(DotEnvUser.email, DotEnvUser.api)) {
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
    String parsedJiraIssues = parseJiraIssues(httpRequestAndResponse.response.body(), versionLower, patchInput);
    if (!parsedJiraIssues.isEmpty()) {
      StringBuilder finalStr = new StringBuilder();
      finalStr.append(writeIntro(patchInput, versionLower, versionHigher, versionInput, strDate))
          .append(parsedJiraIssues)
          .append(writeOutro(patchInput, versionHigher));
      writeToFile(finalStr.toString(), fileName);
    } else {
      LOGGER.fatal("No issues found with that label");
    }
  }
}
