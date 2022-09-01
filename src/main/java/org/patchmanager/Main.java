package org.patchmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import static org.patchmanager.menu.MainMenu.mainMenu;


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
    long start = System.currentTimeMillis();
    Logger LOGGER = null;
    new DotEnvUser();
    try {
      LOGGER = LogManager.getLogger(Main.class);
    } catch (Exception e) {
      System.out.println("log4j2.xml cannot be found");
      System.exit(-1);
    }
    mainMenu();
    /*Scanner scanner = new Scanner(System.in);
    ServerUser labUsr = new ServerUser("ntsysadm","47.168.150.36", DotEnvUser.labpassword);
    ServerUser zitsvyUsr = new ServerUser("senas","10.254.51.215",DotEnvUser.zitsvypassword,22);
    String terminalUser;
    String switchValue = "noValue";

    LOGGER.debug("Started the main function");
    String labelInput = "";
    String versionInput = "";
    String patchInput = "";
    OptionsRelated optionsRelatedObj = new OptionsRelated();
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
      //if the checker methods are turned to single methods with try catch, tests in IntelliJ terminates without testing
      try {
        LOGGER.info("Checking if there is a missing option");
        switchValue = missingOptionChecker(commandLine, switchValue);
      } catch (Exception e) {
        LOGGER.fatal("There is a missing option");
        printHelpCmd(optionsList);
        System.exit(-1);
      }

      if (switchValue.equals("JiraIssueToText")) {
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
      }
    } catch (Exception e) {
      LOGGER.fatal(e.getMessage());
    }


    if (switchValue.equals("JiraIssueToText")) {
      LOGGER.info("Jira Issue to Text Service Starting");
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
    } else if (switchValue.equals("TERMINAL")) {
      terminalUser = commandLine.getOptionValue(TERMINAL);
      LOGGER.info("Terminal Service Starting");
      if (terminalUser.equals("labUsr")){
        LOGGER.info("Connecting to lab");
        new MaverickTerminal().maverickPipedShell(labUsr);
      }else {
        LOGGER.info("Connecting to zitsvy");
        new MaverickTerminal().maverickPipedShell(zitsvyUsr);
      }

    } else if (switchValue.equals("MVNINSTALL")) {
      LOGGER.info("Maven build and transfer war file");
      new MaverickBuildAndTransfer().maverickBuildAndTransfer(zitsvyUsr);
    } else if (switchValue.equals("FCCREATION")) {
      LOGGER.info("FC Creation");
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
        LOGGER.info("Checking if the patch increase value is in correct form");
        patchIncreaseInputChecker(commandLine);
      } catch (Exception e) {
        LOGGER.fatal("Patch increase value does not fit the criteria");
        printHelpCmd(optionsList);
        System.exit(-1);
      }
      String patchIncrease = commandLine.getOptionValue(FCCREATION);
      new MaverickFCCreation().maverickFCCreation(zitsvyUsr, versionInput, patchIncrease);

    } else if (switchValue.equals("PATCHCREATION")) {
      LOGGER.info("Testing newclass");
      new NewClass2().newClass2(labUsr);
    }
     */
    LOGGER.info("Finishing the main function");
    //check if fc and patch files exists
    long finish = System.currentTimeMillis();
    double timeElapsed = finish - start;
    System.out.println(timeElapsed/1000);
    System.out.println("seconds passed");
  }
//make password masked
  //make parse string response shorter
  //check style
  //check performance of long regex
  //username sifre input
  //scp transfel destination user ip password directory
  //rc 0
  //4.8.1.dm64te load numarası hangisi
  //fix initial loggers
  //jira issue printlerken version verme
  //tam olarak fc hesaplanması
  //zitsvyden sadece sftp, labdan sadece scp

  //fc yoksa ilk patch
  //--fc al
  //--userdan patch noyu al
}