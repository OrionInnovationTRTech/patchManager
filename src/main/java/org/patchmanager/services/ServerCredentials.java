package org.patchmanager.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.util.Scanner;

import static org.patchmanager.services.CheckConnection.checkConnection;

public class ServerCredentials {
  private ServerCredentials(){
    throw new IllegalStateException("Utility class");
  }
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
    password = String.valueOf(System.console().readPassword());
    ServerUser newUser = new ServerUser(username, ip, password);

    if (checkConnection(newUser)){
      LOGGER.info("New user created");
      return newUser;
    }else{
      LOGGER.info("Problem creating the user, enter credentials again and check VPN");
      return null;
    }
  }
}
