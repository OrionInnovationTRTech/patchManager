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
import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;

public class CheckMultipleFCsExist {
  static Logger LOGGER = LogManager.getLogger(CheckMultipleFCsExist.class);

  /**
   * Checks if fc file exists in patch directory, does it by searching in ls FC_versionHigher e.g. FC_9.8.1
   * @param serverUser
   * @param versionHigher
   * @return name of fc file if it is there, -1 if not found
   * @throws IOException
   * @throws SshException
   */
  public static String checkMultipleFCsExist(ServerUser serverUser, String versionHigher) throws IOException {
    final String[] nameOfFC = {"-1" , "-1"};
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
          int startingIndexOfFC1 = line.indexOf("FC_"+versionHigher);
          int endingIndexOfFC1 = -1;

          if (startingIndexOfFC1 != -1){
            endingIndexOfFC1 = line.indexOf(" ", startingIndexOfFC1);
            nameOfFC[0] = line.substring(startingIndexOfFC1,endingIndexOfFC1);
          }

          int startingIndexOfFC2 = line.indexOf("FC_"+versionHigher, endingIndexOfFC1);
          int endingIndexOfFC2 = -1;

          if (startingIndexOfFC2 != -1){
            endingIndexOfFC2 = line.indexOf(" ", startingIndexOfFC2);
            nameOfFC[1] = line.substring(startingIndexOfFC2,endingIndexOfFC2);
          }


        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
    //name of second fc file
    return nameOfFC[1];
  }
}
