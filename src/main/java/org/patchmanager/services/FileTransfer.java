package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class FileTransfer {
  static String sourcePath = "";
  static String sourceIp = "";
  static String sourceUsername = "";
  static String sourcePassword = "";

  static String destinationPath = "";
  static String destinationIp = "";
  static String destinationUsername = "";
  static String destinationPassword = "";
  static String sourceOrDestinationMachine = "";
  static Logger LOGGER = LogManager.getLogger(FileTransfer.class);
  public static void fileTransfer(ServerUser serverUser) throws IOException, SshException {
    LOGGER.info("File transfer service started");
    Scanner scanner = new Scanner(System.in);


    sourceIp = serverUser.getIp();
    sourcePassword = serverUser.getPassword();
    sourceUsername = serverUser.getUsername();

    System.out.print("Enter the path of the file that is going to be transferred: ");
    sourcePath = scanner.nextLine();

    System.out.print("Enter the username of destination machine: ");
    destinationUsername = scanner.nextLine();

    System.out.print("Enter the ip of destination machine: ");
    destinationIp = scanner.nextLine();

    System.out.print("Enter the password of destination machine: ");
    destinationPassword = String.valueOf(System.console().readPassword());

    System.out.print("Enter the file path of destination machine that the file is going to be transferred to: ");
    destinationPath = scanner.nextLine();


    //scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war ntsysadm@47.168.150.36:/tmp/
    sourcePath = "/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war";
    destinationUsername = "ntsysadm";
    destinationIp = "47.168.150.36";
    destinationPath = "/tmp";
    destinationPassword = "RAPtor1234";
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
          if(controller.expect("senas@10.254.51.215's password:",false)){
            System.out.println("Enter password");
            controller.typeAndReturn(scanner.nextLine());
          }
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
}
