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
import org.jetbrains.annotations.NotNull;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.cli.PatchInputChecker.patchInputChecker;
import static org.patchmanager.cli.VersionInputChecker.versionInputChecker;
import static org.patchmanager.maverickshhutils.IncrementLoadNo.incrementLoadNo;
import static org.patchmanager.maverickshhutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;
import static org.patchmanager.services.CheckFCExists.checkFCExists;
import static org.patchmanager.services.LoadNumberInputChecker.loadNumberInputChecker;

public class PatchCreation {
  static Logger LOGGER = LogManager.getLogger(PatchCreation.class);
  static String gitBranch = "";
  static String labelInput = "";
  static String versionInput = "";
  static String patchInput = "";
  //get the numbers
  //get the patch no
  // get version
  public static void patchCreation(ServerUser serverUser) throws IOException, SshException {

    Scanner scanner = new Scanner(System.in);
    LOGGER.info("Starting Service for creating patch file");
    System.out.print("Enter git branch to checkout to: ");
    gitBranch = scanner.nextLine();

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

    int noOfIssues = numberOfIssues(labelInput);

    String[] splitVersionByDot = versionInput.split("\\.");
    String loadNo = splitVersionByDot[3];
    String versionHigher = String.join(".", splitVersionByDot[0], splitVersionByDot[1], splitVersionByDot[2]);
    String increasedLoadNo = incrementLoadNo(loadNo, noOfIssues);
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info("Sending git checkout " + gitBranch);
          printCommandOutputLines(shell.executeCommand("git checkout " + gitBranch));
          LOGGER.info("Sending git pull");
          printCommandOutputLines(shell.executeCommand("git pull"));
          String fc = checkFCExists(serverUser, versionHigher);
          //if first patch
          if (patchInput.equals("1")) {

            //if there is no fc file and first patch
            if (fc.equals("-1")) {
              System.out.print("Please enter the load number of the FC file");
              String loadNumberOfFcForCreation = scanner.nextLine();
              LOGGER.info("Changing aa01 in pom.xml to: " + loadNumberOfFcForCreation);
              printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ loadNumberOfFcForCreation +"/' ../pom.xml"));
              LOGGER.info("Running FC script");
              ShellProcess fcProcess;
              printCommandOutputLines(fcProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m FC"));
              LOGGER.info("FC script process ended with exit code: " + fcProcess.getExitCode());
              LOGGER.info("Getting number of issues");
              int numberOfIssues = numberOfIssues(labelInput);
              String increasedLoad = incrementLoadNo(loadNumberOfFcForCreation ,numberOfIssues);
              LOGGER.info("Changing "+ loadNumberOfFcForCreation +" in pom.xml to: " + increasedLoad);
              printCommandOutputLines(shell.executeCommand("sed -i.bak 's/"+loadNumberOfFcForCreation+"/"+ increasedLoad +"/' ../pom.xml"));
              LOGGER.info("Running patch script");
              ShellProcess patchProcess;
              printCommandOutputLines(patchProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ "1" +" -c FC_"+ versionHigher +"."+ loadNumberOfFcForCreation +"_Checksums.txt"));
              LOGGER.info("FC script process had ended with exit code: " + fcProcess.getExitCode());
              LOGGER.info("Patch script process ended with exit code: " + patchProcess.getExitCode());
            }
            //if there is fc file and first patch
            else {
              String fcLoadNumber = getFcLoadNumber(fc);
              LOGGER.info("Load number of FC is "+ fcLoadNumber);
              LOGGER.info("Getting number of issues");
              int numberOfIssues = numberOfIssues(labelInput);
              String increasedLoad = incrementLoadNo(fcLoadNumber ,numberOfIssues);
              LOGGER.info("FC load number was "+ fcLoadNumber);
              LOGGER.info("Number of issues with the patch label is " +numberOfIssues);
              LOGGER.info("Load number of patch 1 will be "+ increasedLoad);
              LOGGER.info("Changing "+ "aa01" +" in pom.xml to: " + increasedLoad);
              printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ increasedLoad +"/' ../pom.xml"));
              LOGGER.info("Running patch script");
              ShellProcess patchProcess;
              printCommandOutputLines(patchProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ "1" +" -c FC_"+ versionHigher +"."+ fcLoadNumber +"_Checksums.txt"));
              LOGGER.info("Patch script process ended with exit code: " + patchProcess.getExitCode());
            }
          //if it is not the first patch, also means that there is also an fc file
          }else {
            String fcLoadNumber = getFcLoadNumber(fc);
            LOGGER.info("Load number of FC is "+ fcLoadNumber);
            LOGGER.info("Getting number of issues");
            int numberOfIssues = numberOfIssues(labelInput);

            System.out.println("Please enter the load number of the patch "+ (Integer.parseInt(patchInput) - 1) + " e.g. dm74");
            String loadNumberOfPreviousPatch = scanner.nextLine();

            while(!loadNumberInputChecker(loadNumberOfPreviousPatch)){
              System.out.print("Wrong load number format, enter again: ");
              loadNumberOfPreviousPatch = scanner.nextLine();
            }

            String increasedLoad = incrementLoadNo(loadNumberOfPreviousPatch ,numberOfIssues);
            LOGGER.info("FC load number was "+ fcLoadNumber);
            LOGGER.info("Number of issues of the previous patch is " +loadNumberOfPreviousPatch);
            LOGGER.info("Number of issues with the patch label is " +numberOfIssues);
            LOGGER.info("Load number of patch "+patchInput+" will be "+ increasedLoad);
            LOGGER.info("Changing "+ "aa01" +" in pom.xml to: " + increasedLoad);
            printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ increasedLoad +"/' ../pom.xml"));
            LOGGER.info("Running patch script");
            ShellProcess patchProcess;
            printCommandOutputLines(patchProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ patchInput +" -c FC_"+ versionHigher +"."+ fcLoadNumber +"_Checksums.txt"));
            LOGGER.info("Patch script process ended with exit code: " + patchProcess.getExitCode());
          }
        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }

  @NotNull
  private static String getFcLoadNumber(String fc) {
    //FC_9.8.1.dm64_Checksums.txt to FC,9.8.1.dm64,Checksums.txt
    String[] fcSplitted = fc.split("_");
    LOGGER.info("The fc version is "+ fcSplitted[1]);
    //9.8.1.dm64 to just get the last 4 things
    String fcLoadNumber = fcSplitted[1].substring(fcSplitted[1].length() - 4);
    return fcLoadNumber;
  }
}
