package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.sftp.SftpClient;
import com.sshtools.client.sftp.SftpClientTask;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.permissions.PermissionDeniedException;
import com.sshtools.common.sftp.SftpStatusException;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;
import static org.patchmanager.services.CheckFCExists.checkFCExists;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class TestNewService {
  static Logger LOGGER = LogManager.getLogger(TestNewService.class);
  static String sourcePath = "";
  static String sourceIp = "";
  static String sourceUsername = "";
  static String sourcePassword = "";

  static String destinationPath = "";
  static String destinationIp = "";
  static String destinationUsername = "";
  static String destinationPassword = "";
  public static void testNewService(ServerUser serverUser) throws IOException, SshException {
    Scanner scanner = new Scanner(System.in);
    ServerUser destinationUser = null;
    while(destinationUser == null){
      System.out.println("Enter the info of the destination server");
      destinationUser = serverUserCredentials();
    }
    sourceIp = serverUser.getIp();
    sourcePassword = serverUser.getPassword();
    sourceUsername = serverUser.getUsername();
    sourcePath = "/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war";
    destinationUsername = destinationUser.getUsername();
    destinationIp = destinationUser.getIp();
    destinationPath = "/tmp";
    destinationPassword = destinationUser.getPassword();
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          LOGGER.info("Sending scp");
          ShellProcessController controller =  new ShellProcessController(
              shell.executeCommand("scp " + sourceUsername + "@" + sourceIp + ":" + sourcePath +  " "
                  + destinationUsername + "@" + destinationIp + ":" + destinationPath));
          waitFor(1000);
          LOGGER.info("Sending the password of the source machine");
          controller.typeAndReturn(sourcePassword);
          waitFor(1000);
          LOGGER.info("Sending the password of the remote machine");
          controller.typeAndReturn(destinationPassword);
          waitFor(1000);
          LOGGER.info("Doing the file transfer");
          controller.getProcess().drain();
          System.out.println(controller.getProcess().getCommandOutput());
          System.out.println("Transfer ended with exit code: " + controller.getProcess().getExitCode());
          LOGGER.info("File transfer ended");
        }
      });
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }

  static String ntpas = "wae_java_version =\n" +
      "WAE CSH git environment setup complete.\n" +
      "Setting up JAVA: JAVA_HOME=/opt/java/jdk1.7.0_121\n" +
      "Setting up ANT: ANT_HOME=/opt/tools/wh/dtd/tools/ant/1.7.1\n" +
      "Setting up M2_HOME=/opt/tools/wh/dtd/tools/maven/2.2.1\n" +
      "wae_java_version = 1.7.0_121\n" +
      "WAE CSH git environment setup complete.\n" +
      "Setting up JAVA: JAVA_HOME=/opt/java/jdk1.7.0_121\n" +
      "Setting up ANT: ANT_HOME=/opt/tools/wh/dtd/tools/ant/1.7.1\n" +
      "Setting up M2_HOME=/opt/tools/wh/dtd/tools/maven/2.2.1\n" +
      "\n" +
      "Authorized users only. All activity may be monitored and reported.\n" +
      "\n" +
      "ntsysadm@47.168.150.36's password:";
  //check different expect versions
  //check password checking
  //check if dest path is wrong
  //check if source patch is wrong
}
