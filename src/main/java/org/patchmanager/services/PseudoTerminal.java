package org.patchmanager.services;

import com.sshtools.client.*;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;

public class PseudoTerminal {
  private PseudoTerminal(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(PseudoTerminal.class);

  /**
   * Pseudoterminal that can run simple commands like ls or pwd
   * @param serverUser
   * @throws IOException
   */
  public static void pseudoTerminal(ServerUser serverUser) throws IOException {
    Scanner scanner = new Scanner(System.in);
    LOGGER.info("Starting pseudoterminal service");
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          System.out.println(ssh.executeCommand("whoami"));
          ExpectShell shell = new ExpectShell(this);
          String cmd = "";
          while(session.isConnected()){
            System.out.print("InputCommand> ");
            cmd = scanner.nextLine();
            printCommandOutputLines(shell.executeCommand(cmd));

          }
          System.out.println("Terminating");
        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }

}
