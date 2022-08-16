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
import java.util.Scanner;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class MaverickPipedShell {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverickPipedShell(ServerUser serverUser) throws IOException, SshException {
    Scanner scanner = new Scanner(System.in);
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          String cmd = "";
          while(session.isConnected()){
            System.out.print(">");
            cmd = scanner.nextLine();
            ShellProcess process;
            ShellProcessController controller = new ShellProcessController(process = shell.executeCommand(cmd));
            printCommandOutputLines(process);
          }
          System.out.println("Terminating");
        }
      });
    };
  }

}
