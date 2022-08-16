package org.patchmanager.maverickshhutils;

import com.sshtools.client.*;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.sshutils.ServerUser;

import java.io.IOException;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class MaverickFCCreation {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverickFCCreation(ServerUser serverUser) throws IOException, SshException {

    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          printCommandOutputLines(shell.executeCommand("git checkout spidr_4.8.1_patch"));
          printCommandOutputLines(shell.executeCommand("git pull"));
          printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
          printCommandOutputLines(shell.executeCommand("whoami"));
        }
      });
    };
  }

}
