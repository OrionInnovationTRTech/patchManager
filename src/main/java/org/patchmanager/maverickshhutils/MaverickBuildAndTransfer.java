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

public class MaverickBuildAndTransfer {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverickBuildAndTransfer(ServerUser serverUser) throws IOException, SshException {
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("cdwae"));
          printCommandOutputLines(shell.executeCommand("cd base/modules/webapps/wae-admin-rest-war/"));
          printCommandOutputLines(shell.executeCommand("git checkout spidr_4.8.1_patch"));
          printCommandOutputLines(shell.executeCommand("git pull"));
          printCommandOutputLines(shell.executeCommand("whoami"));
          ShellProcess process;
          ShellProcessController controller =  new ShellProcessController(
              process = shell.executeCommand("scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war  ntsysadm@47.168.150.36:/tmp/"));
          waitFor(500);
          controller.typeAndReturn(DotEnvUser.zitsvypassword);
          waitFor(500);
          controller.typeAndReturn(DotEnvUser.labpassword);
          printCommandOutputLines(process);
        }
      });
    };
  }

}
