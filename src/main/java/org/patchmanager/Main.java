package org.patchmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.sshutils.*;


/**
 * Main class that links all the methods.
 */
public class Main {
  /**
   * Main method that runs the patchManager.
   *
   * @param args cli args, they are supposed to be
   *             --version --label --patch or their short versions
   */
  public static void main(String[] args) throws Exception {
    Logger LOGGER = null;
    try {
      LOGGER = LogManager.getLogger(Main.class);
    } catch (Exception e) {
      System.out.println("log4j2.xml cannot be found");
      System.exit(-1);
    }
    LOGGER.debug("Started the main function");
    ExecuteCodeMina t = new ExecuteCodeMina();
    //MinaTest mt = new MinaTest();
    InteractiveCmdMina ic = new InteractiveCmdMina();
    System.out.println(ic.getFileList());
    //ScpMina sc = new ScpMina();
   // DownloadFileSftp d = new DownloadFileSftp();
    /**
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
    String parsedJiraIssues = parseJiraIssues(httpRequestAndResponse.response.body(), versionLower, patchInput);
    if (!parsedJiraIssues.isEmpty()) {
      StringBuilder finalStr = new StringBuilder();
      finalStr.append(writeIntro(patchInput, versionLower, versionHigher, versionInput, strDate))
          .append(parsedJiraIssues)
          .append(writeOutro(patchInput, versionHigher));
      writeToFile(finalStr.toString(), fileName);
    } else {
      LOGGER.fatal("No issues found with that label");
      System.exit(-1);
    }
    LOGGER.info("Finishing the main function");
     **/
  }
  //+gitignore
  //make parse string response shorter
  //check style
  //+parse check first 15 chars only
  //check performance of long regex
  //+try catch null option missing
  //+mockito
  //+api test
  //+change name of classes to capital starting letter
  //+display path and success
  //+two chars
  //+remove [] things and such
  //+log4j
}