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
import java.nio.channels.UnresolvedAddressException;
import java.util.Scanner;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class CheckConnection {
  static Logger LOGGER = LogManager.getLogger(CheckConnection.class);
  public static boolean checkConnection(ServerUser serverUser) {
    LOGGER.info("Starting the service to check connection");

    try (SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override
        protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
        }
      });
      LOGGER.info("Can connect to server");
      return true;
    } catch (IOException ioe) {
      System.out.println("Problem with io");
      LOGGER.fatal(ioe.getMessage());
      return false;
    } catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
      return false;
    } catch (UnresolvedAddressException uaee) {
      System.out.println("Problem with address");
      LOGGER.fatal(uaee.getMessage());
      return false;
    }
  }
}
