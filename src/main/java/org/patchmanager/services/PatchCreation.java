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

import static org.patchmanager.inputcheckers.PatchInputChecker.patchInputChecker;
import static org.patchmanager.inputcheckers.VersionBaseInputChecker.versionBaseInputChecker;
import static org.patchmanager.mavericksshutils.GetFCLoadNumber.getFcLoadNumber;
import static org.patchmanager.mavericksshutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;
import static org.patchmanager.services.CheckFCExists.checkFCExists;
import static org.patchmanager.services.CheckMultipleFCsExist.checkMultipleFCsExist;
import static org.patchmanager.services.CreatePatch.createPatch;
import static org.patchmanager.inputcheckers.LoadNumberInputChecker.loadNumberInputChecker;

public class PatchCreation {
  private PatchCreation(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(PatchCreation.class);
  static String gitBranch = "";
  static String labelInput = "";
  static String versionBaseInput = "";
  static String patchInput = "";
  public static void patchCreation(ServerUser serverUser) throws IOException, SshException {

    Scanner scanner = new Scanner(System.in);
    LOGGER.info("Starting Service for creating patch file");
    System.out.print("Enter git branch to checkout to: ");
    gitBranch = scanner.nextLine();

    System.out.print("Enter the label: ");
    labelInput = scanner.nextLine();

    System.out.print("Enter the version in the form of 9.8.1: ");
    versionBaseInput = scanner.nextLine();
    while(!versionBaseInputChecker(versionBaseInput)){
      System.out.print("Wrong version format, enter again: ");
      versionBaseInput = scanner.nextLine();
    }

    System.out.print("Enter the patch in the form of an integer: ");
    patchInput = scanner.nextLine();
    while(!patchInputChecker(patchInput)){
      System.out.print("Wrong patch format, enter again: ");
      patchInput = scanner.nextLine();
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
          String secondFc = checkMultipleFCsExist(serverUser,versionBaseInput);
          if (!secondFc.equals("-1")){
            LOGGER.fatal("There are multiple FC files found, terminating");
            System.exit(-1);
          }

          String fc = checkFCExists(serverUser, versionBaseInput);
          //if first patch
          if (patchInput.equals("1")) {
            //if there is no fc file and first patch
            if (fc.equals("-1")) {
              LOGGER.fatal("No FC file found while trying create a patch");
              System.exit(-1);
            }
            //if there is fc file and it is the first patch
            else {
              String fcLoadNumber = getFcLoadNumber(fc);
              LOGGER.info("Load number of FC is {}", fcLoadNumber);
              LOGGER.info("Getting number of issues");
              int numberOfIssues = numberOfIssues(labelInput);

              ShellProcess patchProcess = createPatch(shell, fcLoadNumber,
                  numberOfIssues, fcLoadNumber, versionBaseInput, patchInput,"aa01");
              LOGGER.info("Patch script process ended with exit code: {}" , patchProcess.getExitCode());
            }
          //If it is not the first patch
          }else {
            //if it is not the first patch and there is no fc file
            if (fc.equals("-1")){
              LOGGER.fatal("No FC file found while trying create a patch");
              System.exit(-1);
              //if it is not the first patch and there is a fc file
            }else {
              String fcLoadNumber = getFcLoadNumber(fc);
              LOGGER.info("Load number of FC is {}" , fcLoadNumber);
              LOGGER.info("Getting number of issues");
              int numberOfIssues = numberOfIssues(labelInput);

              System.out.println("Please enter the load number of the previous patch " + (Integer.parseInt(patchInput) - 1) + " e.g. dm74 for the previous patch");
              String loadNumberOfPreviousPatch = scanner.nextLine();
              while (!loadNumberInputChecker(loadNumberOfPreviousPatch)) {
                System.out.print("Wrong load number format, enter again: ");
                loadNumberOfPreviousPatch = scanner.nextLine();
              }
              ShellProcess patchProcess = createPatch(shell, loadNumberOfPreviousPatch,
              numberOfIssues, fcLoadNumber, versionBaseInput, patchInput, "aa01");
              LOGGER.info("Patch script process ended with exit code: {}" , patchProcess.getExitCode());
            }
          }
        }
      });
    }catch (SshException sshe) {
      LOGGER.fatal("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }
}
