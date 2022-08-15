package org.patchmanager.sshutils;

import com.sshtools.client.*;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.scp.ScpCommand;
import com.sshtools.common.ssh.SshConnection;
import com.sshtools.common.ssh.SshException;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.patchmanager.sshutils.PrintCommandOutputLines.printCommandOutputLines;

public class Maverick {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverick(ServerUser serverUser) throws IOException, SshException {
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          /*printCommandOutputLines(shell.executeCommand("gitwaeall"));
          printCommandOutputLines(shell.executeCommand("git checkout spidr_4.8.1_patch"));
          printCommandOutputLines(shell.executeCommand("git pull"));*/
          /*printCommandOutputLines(shell.executeCommand("ls -lh"));*/
          ShellProcess process;
          ShellProcessController controller =  new ShellProcessController(
              process = shell.executeCommand("scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/loadbuild/patch/Patch_2_Checksums_9.8.1.dm95.txt  ntsysadm@47.168.150.36:/tmp/"));
          waitFor(1000);
          controller.typeAndReturn(DotEnvUser.zitsvypassword);
          waitFor(1000);
          controller.typeAndReturn(DotEnvUser.labpassword);

          //controller.getProcess().getOutputStream().write(DotEnvUser.labpassword.getBytes());
          //controller.getProcess().getOutputStream().flush();
          printCommandOutputLines(process);
        }
      });
    };
  }

}
