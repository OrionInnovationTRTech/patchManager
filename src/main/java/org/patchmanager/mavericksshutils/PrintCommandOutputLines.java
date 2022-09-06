package org.patchmanager.mavericksshutils;

import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.common.ssh.SshException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class PrintCommandOutputLines {
  private PrintCommandOutputLines(){
    throw new IllegalStateException("Utility class");
  }
  public static void printCommandOutputLines(ShellProcess process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while((line = reader.readLine())!=null) {
      System.out.println(line);
    }
  }

  public static void printCommandOutputLines( ShellProcessController controller) throws IOException, ShellTimeoutException, SshException, InterruptedException {
    Scanner scanner = new Scanner(System.in);
    if(controller.expect("Password")) {
      System.out.println("Enter password ");
      String pass = scanner.nextLine();
      controller.typeAndReturn(pass);
      controller.wait(500);
    }
    controller.getProcess().drain();
    System.out.println(controller.getProcess().getCommandOutput());
  }

  /**
   * prints char by char instead of line by line to to make sure dots in analyzing pom file is printed on screen
   * @param process
   * @throws IOException
   */
  public static void charPrintCommandOutputLines(ShellProcess process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    while(process.isActive()) {
      System.out.print((char)reader.read());
    }
  }
}
