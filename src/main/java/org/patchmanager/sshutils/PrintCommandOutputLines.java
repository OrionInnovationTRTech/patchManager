package org.patchmanager.sshutils;

import com.sshtools.client.shell.ShellProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PrintCommandOutputLines {
  public static void printCommandOutputLines(ShellProcess process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while((line = reader.readLine())!=null) {
      System.out.println(line);
    }
  }
}
