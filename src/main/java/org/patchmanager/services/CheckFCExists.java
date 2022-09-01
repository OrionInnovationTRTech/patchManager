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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static org.patchmanager.cli.PatchInputChecker.patchInputChecker;
import static org.patchmanager.cli.VersionInputChecker.versionInputChecker;
import static org.patchmanager.maverickshhutils.IncrementLoadNo.incrementLoadNo;
import static org.patchmanager.maverickshhutils.NumberOfIssues.numberOfIssues;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class CheckFCExists {
  static Logger LOGGER = LogManager.getLogger(CheckFCExists.class);

  /**
   * Checks if fc file exists in patch directory, does it by searching in ls FC_versionHigher e.g. FC_9.8.1
   * @param serverUser
   * @param versionHigher
   * @return name of fc file if it is there, -1 if not found
   * @throws IOException
   * @throws SshException
   */
  public static String checkFCExists(ServerUser serverUser, String versionHigher) throws IOException, SshException {
    final String[] nameOfFC = {"-1"};
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          LOGGER.info("Sending gitwaeall");
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info("Sending cd ..");
          printCommandOutputLines(shell.executeCommand("cd .."));
          LOGGER.info("Sending cd patch");
          printCommandOutputLines(shell.executeCommand("cd patch"));
          LOGGER.info("Sending ls");
          ShellProcess process = shell.executeCommand("ls");
          //finish the process
          process.drain();
          String line = process.getCommandOutput();
          int startingIndexOfFC = line.indexOf("FC_"+versionHigher);
          int endingIndexOfFC;

          if (startingIndexOfFC != -1){
            endingIndexOfFC = line.indexOf(" ", startingIndexOfFC);
            nameOfFC[0] = line.substring(startingIndexOfFC,endingIndexOfFC);
          }

        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
    return nameOfFC[0];
  }
}
