package org.patchmanager.maverickshhutils;

import com.sshtools.client.*;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.IOException;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class MaverickRunCommand {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverickRunCommand(ServerUser serverUser, String command) throws IOException, SshException {
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand(command));
        }
      });
    };
  }

}
