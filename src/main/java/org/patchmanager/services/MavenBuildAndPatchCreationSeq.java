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
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.cli.PatchInputChecker.patchInputChecker;
import static org.patchmanager.cli.VersionInputChecker.versionInputChecker;
import static org.patchmanager.maverickshhutils.IncrementLoadNo.incrementLoadNo;
import static org.patchmanager.maverickshhutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class MavenBuildAndPatchCreationSeq {
  static String gitBranch = "";
  static String labelInput = "";
  static String versionInput = "";
  static String patchInput = "";
  static Logger LOGGER = LogManager.getLogger(MavenBuildAndPatchCreationSeq.class);
  public static void mavenBuildAndPatchCreationSeq(ServerUser serverUser) {
    LOGGER.info("Starting Service to take maven build and create patch files");
    System.out.print("Enter git branch to checkout to: ");
    Scanner scanner = new Scanner(System.in);
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
    LOGGER.info("Getting number of issues");
    int noOfIssues = numberOfIssues(labelInput, patchInput);

    String[] splitVersionByDot = versionInput.split("\\.");
    String loadNo = splitVersionByDot[3];
    String versionHigher = String.join(".", splitVersionByDot[0], splitVersionByDot[1], splitVersionByDot[2]);
    String increasedLoadNo = incrementLoadNo(loadNo, noOfIssues);

    try (SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override
        protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          LOGGER.info("Sending gitwaeall");
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info("Sending git checkout " + gitBranch);
          printCommandOutputLines(shell.executeCommand("git checkout " + gitBranch));
          LOGGER.info("Sending git pull");
          printCommandOutputLines(shell.executeCommand("git pull"));
          LOGGER.info("Sending mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install");
          printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
          LOGGER.info("Changing aa01 in pom.xml to: " + increasedLoadNo);
          printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ increasedLoadNo +"/' ../pom.xml"));
          LOGGER.info("Running patch script");
          ShellProcess process;
          printCommandOutputLines(process = shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ patchInput +" -c FC_"+ versionHigher +"."+ "dm64" +"_Checksums.txt"));
          LOGGER.info("Changing the pom.xml to previous state");
          printCommandOutputLines(shell.executeCommand("sed -i.bak 's/"+increasedLoadNo+"/aa01/' ../pom.xml"));
          LOGGER.info("Patch script process ended with exit code: " + process.getExitCode());

        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }
}
