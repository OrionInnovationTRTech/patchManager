package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.cli.PatchInputChecker.patchInputChecker;
import static org.patchmanager.cli.VersionInputChecker.versionInputChecker;
import static org.patchmanager.maverickshhutils.IncrementLoadNo.incrementLoadNo;
import static org.patchmanager.maverickshhutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class PatchCreation {
  static String labelInput = "";
  static String versionInput = "";
  static String patchInput = "";
  //get the numbers
  //get the patch no
  // get version
  public static void patchCreation(ServerUser serverUser) throws IOException, SshException {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the label: ");
    labelInput = scanner.nextLine();

    System.out.println("Enter the version in the form of 9.8.1.dm10: ");
    versionInput = scanner.nextLine();
    while(versionInputChecker(versionInput)){
      System.out.println("Wrong version format, enter again: ");
      versionInput = scanner.nextLine();
    }

    System.out.println("Enter the patch in the form of an integer: ");
    patchInput = scanner.nextLine();
    while(patchInputChecker(patchInput)){
      System.out.println("Wrong patch format, enter again: ");
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
          printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ increasedLoadNo +"/' ../pom.xml"));
          printCommandOutputLines(shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ patchInput +" -c FC_"+ versionHigher +"."+ "dm64" +"_Checksums.txt"));
          printCommandOutputLines(shell.executeCommand("sed -i.bak 's/dm67/"+ "aa01" +"/' ../pom.xml"));
        }
      });
      //patch 1 degilse
      //GA 4.8.1.dm10
      //passwordu alma
    };
  }
}
