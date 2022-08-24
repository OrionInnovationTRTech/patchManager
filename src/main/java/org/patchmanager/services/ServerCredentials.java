package org.patchmanager.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.maverickshhutils.ServerUser;

import java.util.Scanner;

import static org.patchmanager.cli.VersionInputChecker.versionInputChecker;

public class ServerCredentials {
  static Logger LOGGER = LogManager.getLogger(ServerCredentials.class);
  public static ServerUser serverUserCredentials(){
    Scanner scanner = new Scanner(System.in);
    String username = "";
    String ip = "";
    String password = "";
    System.out.print("Enter username: ");
    username = scanner.nextLine();

    System.out.print("Enter ip: ");
    ip = scanner.nextLine();

    System.out.print("Enter password: ");
    password = scanner.nextLine();
    return new ServerUser(username, ip, password);
  }
}
