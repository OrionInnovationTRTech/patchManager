package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.inputcheckers.LoadNumberInputChecker.loadNumberInputChecker;
import static org.patchmanager.mavericksshutils.GetFCLoadNumber.getFcLoadNumber;
import static org.patchmanager.mavericksshutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;
import static org.patchmanager.services.CheckFCExists.checkFCExists;
import static org.patchmanager.services.CreatePatch.createPatch;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class TestNewService {
  static Logger LOGGER = LogManager.getLogger(TestNewService.class);
  static String sourcePath = "";
  static String sourceIp = "";
  static String sourceUsername = "";
  static String sourcePassword = "";

  static String destinationPath = "";
  static String destinationIp = "";
  static String destinationUsername = "";
  static String destinationPassword = "";
  public static void testNewService(ServerUser serverUser) throws IOException, SshException {
    Scanner scanner = new Scanner(System.in);
    String gitBranch = "spidr_4.8.1_patch";
    String versionBaseInput = "9.8.1";
    String labelInput = "KL_4.8.1_P_4";
    String patchInput = "4";
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info("Sending git checkout " + gitBranch);
          printCommandOutputLines(shell.executeCommand("git checkout " + gitBranch));
          LOGGER.info("Sending git pull");
          printCommandOutputLines(shell.executeCommand("git pull"));

          String fc = checkFCExists(serverUser, versionBaseInput);
          String fcLoadNumber = getFcLoadNumber(fc);
          LOGGER.info("Load number of FC is " + fcLoadNumber);
          LOGGER.info("Getting number of issues");
          int numberOfIssues = numberOfIssues(labelInput);
          String loadNumberOfPreviousPatch = "dm50";
          ShellProcess patchProcess = createPatch(shell, loadNumberOfPreviousPatch,
              numberOfIssues, fcLoadNumber, versionBaseInput, patchInput, "aa01");
          LOGGER.info("Patch script process ended with exit code: " + patchProcess.getExitCode());
        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }
}
