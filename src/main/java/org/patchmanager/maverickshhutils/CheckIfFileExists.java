package org.patchmanager.maverickshhutils;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;

import java.io.IOException;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class CheckIfFileExists {
  public boolean checkIfFileExists(ServerUser serverUser, String filename){
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("ls"));
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SshException e) {
      throw new RuntimeException(e);
    }
    ;
      return false;
  }
}
