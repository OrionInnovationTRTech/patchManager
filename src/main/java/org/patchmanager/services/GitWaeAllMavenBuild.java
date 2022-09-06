package org.patchmanager.services;

import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;

public class GitWaeAllMavenBuild {
  private GitWaeAllMavenBuild(){
    throw new IllegalStateException("Utility class");
  }
  static String gitBranch = "";
  static Logger LOGGER = LogManager.getLogger(GitWaeAllMavenBuild.class);
  public static void gitwaeallMavenBuild(ServerUser serverUser) {
    LOGGER.info("Starting Service to take maven build in cdwae");
    System.out.println("Enter git branch to checkout to: ");
    Scanner scanner = new Scanner(System.in);
    gitBranch = scanner.nextLine();
    try (SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override
        protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {
          ExpectShell shell = new ExpectShell(this);
          LOGGER.info("Sending gitwaeall");
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          LOGGER.info(String.format("git checkout %s", gitBranch));
          printCommandOutputLines(shell.executeCommand("git checkout " + gitBranch));
          LOGGER.info("Sending git pull");
          printCommandOutputLines(shell.executeCommand("git pull"));
          LOGGER.info("Sending mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install");
          ShellProcess process;
          printCommandOutputLines(process = shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
          LOGGER.info("Finished maven build with exit code: {}" , process.getExitCode());
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }catch (SshException sshe) {
      System.out.println("Problem with ssh connection");
      LOGGER.fatal(sshe.getMessage());
    }
  }
}
