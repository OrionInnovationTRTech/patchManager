package org.patchmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.sshutils.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
    new DotEnvUser();
    try {
      LOGGER = LogManager.getLogger(Main.class);
    } catch (Exception e) {
      System.out.println("log4j2.xml cannot be found");
      System.exit(-1);
    }
    LOGGER.debug("Started the main function");


    ServerUser labUsr = new ServerUser("ntsysadm","47.168.150.36", DotEnvUser.labpassword);
    ServerUser zitsvyUsr = new ServerUser("senas","10.254.51.215",DotEnvUser.zitsvypassword,22);
    //new PipedShell(labUsr);

    Scanner scanner = new Scanner(System.in);
    SshClient client = SshClient.setUpDefaultClient();
    ClientSession session = new CreateSshSession().createSshSession(zitsvyUsr, client);
    ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL);
    channel.open().verify(3, TimeUnit.SECONDS);
    new ServerWelcomeMessage().displayWelcomeMessage(channel);
    new PassCmdToChannel().passCmdToChannel("cdwae\n", channel);
    new PassCmdToChannel().passCmdToChannel("cd base/modules/webapps/wae-admin-rest-war/\n", channel);
    new PassCmdToChannel().passCmdToChannel("ls\n", channel);
    new PassCmdToChannel().passCmdToChannel("git checkout spidr_4.8.1_patch\n", channel);
    new PassCmdToChannel().passCmdToChannel("git pull\n", channel);
    new PassCmdToChannel().passCmdToChannel("ls\n", channel);
    //new PassCmdToChannel().passCmdToChannel("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install\n", channel);
    new PassCmdToChannel().passCmdToChannel("ls\n", channel);


    String cmd = "";
    while (true) {
      System.out.println("Write a linux or command or write !! to exit");
      cmd = scanner.nextLine() + "\n";
      if (cmd.equals("!!\n")) {
        System.out.println("Terminating the program");
        break;
      }
      new PassCmdToChannel().passCmdToChannel(cmd, channel);
    }
    channel.close();
    session.close();
    client.close();
    /*new ExecuteCodeZistvy("cdwae; cd base/modules/webapps/wae-admin-rest-war/;git checkout spidr_4.8.1_patch && git pull;mvn -o -s ../settings.xml clean");
    new ExecuteCodeLab("cd /tmp;ls -lha;pwd");
    new TransferWar();
    new ExecuteCodeLab("cd /tmp;ls -lha;pwd");*/
   /* System.out.println("Deleting");
    ExecuteCodeLab l2 = new ExecuteCodeLab("cd /tmp;rm wae-admin-rest-war-9.8.1.war");
    ExecuteCodeLab l3 = new ExecuteCodeLab("cd /tmp;ls -lha;pwd");*/
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