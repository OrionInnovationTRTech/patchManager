package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;

import java.io.IOException;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.MainMenu.*;
import static org.patchmanager.services.FCCreation.fcCreation;
import static org.patchmanager.services.FileTransfer.fileTransfer;
import static org.patchmanager.services.GitWaeAllMavenBuild.gitwaeallMavenBuild;
import static org.patchmanager.services.PatchCreation.patchCreation;
import static org.patchmanager.services.PseudoTerminal.pseudoTerminal;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class ZitsvyMenu {
  private ZitsvyMenu(){
    throw new IllegalStateException("Utility class");
  }
  public static void zitsvyMenu() throws IOException, SshException {
    String choice;
    zitsvyLoop : while(true) {
      displayMenu("Zitsvy Services Menu", ZITSVY_MENU_ITEMS);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextLine();

      switch (choice) {
        case "1"://Enter lab credentials
          zitsvyUsr = serverUserCredentials();
          break;
        case "2"://Pseudoterminal, run commands on zitsvy server
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            pseudoTerminal(zitsvyUsr);
          }
          break;
        case "3"://File transfer
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            fileTransfer(zitsvyUsr);
          }
          break;
        case "4"://Maven Build in gitwaeall
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            gitwaeallMavenBuild(zitsvyUsr);
          }
          break;
        case "5"://FC Creation
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            fcCreation(zitsvyUsr);
          }
          break;
        case "6"://Patch Creation
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            patchCreation(zitsvyUsr);
          }
          break;
        case "7"://Return to main menu
          break zitsvyLoop;
        case "8":
          System.out.println("Terminating the program");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid menu choice; try again.");
          break;
      }
    }
  }
}
