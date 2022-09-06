package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.inputcheckers.VersionBaseInputChecker.versionBaseInputChecker;
import static org.patchmanager.mavericksshutils.CheckIfGreaterThan981.checkIfGreaterThan981;
import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.charPrintCommandOutputLines;
import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;
import static org.patchmanager.services.CheckFCExists.checkFCExists;
import static org.patchmanager.inputcheckers.LoadNumberInputChecker.loadNumberInputChecker;

public class FCCreation {
  private FCCreation(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(FCCreation.class);
  static String gitBranch = "";
  static String versionBaseInput = "";
  public static void fcCreation(ServerUser serverUser) throws IOException, SshException {

    Scanner scanner = new Scanner(System.in);
    LOGGER.info("Starting Service for creating fc file");
    System.out.print("Enter git branch to checkout to for fc creation: ");
    gitBranch = scanner.nextLine();

    System.out.print("Enter the version in the form of 9.8.1: ");
    versionBaseInput = scanner.nextLine();
    while(!versionBaseInputChecker(versionBaseInput)){
      System.out.print("Wrong version format, enter again: ");
      versionBaseInput = scanner.nextLine();
    }


    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info("Sending git checkout {}" , gitBranch);
          printCommandOutputLines(shell.executeCommand("git checkout " + gitBranch));
          LOGGER.info("Sending git pull");
          printCommandOutputLines(shell.executeCommand("git pull"));

          String fc = checkFCExists(serverUser, versionBaseInput);
          //if there is no fc file
          if (fc.equals("-1")) {
            LOGGER.info("No FC file found");
            System.out.print("Please enter the load number of the FC file");
            String loadNumberOfFcForCreation = scanner.nextLine();
            while (!loadNumberInputChecker(loadNumberOfFcForCreation)) {
              System.out.print("Load number invalid enter again: ");
              loadNumberOfFcForCreation = scanner.nextLine();
            }

            printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/" + loadNumberOfFcForCreation + "/' ../pom.xml"));
            LOGGER.info("Sending mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install");
            printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));


            ShellProcess fcProcess;
            if (checkIfGreaterThan981(versionBaseInput)) {
              //run using genKLPatch.sh
              charPrintCommandOutputLines(fcProcess = shell.executeCommand("../patch/genKLPatch.sh -m FC"));
            } else {
              //run using genSpidrPatch.sh
              charPrintCommandOutputLines(fcProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m FC"));
            }
            LOGGER.info("FC script process had ended with exit code: {}" , fcProcess.getExitCode());
            //if there is fc file
          }else{
            LOGGER.fatal("There already is a FC file {}" , fc);
          }

        }
      });
    }catch (SshException sshe) {
      LOGGER.fatal("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }
}
