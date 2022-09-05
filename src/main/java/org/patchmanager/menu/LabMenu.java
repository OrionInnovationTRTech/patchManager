package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;

import java.io.IOException;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.MainMenu.*;
import static org.patchmanager.services.CheckConnection.checkConnection;
import static org.patchmanager.services.PseudoTerminal.pseudoTerminal;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class LabMenu {
  public static void labMenu() throws IOException, SshException {
    String choice;
    labLoop : while(true) {
      displayMenu("Lab Services Menu", LAB_MENU_ITEMS);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextLine();

      switch (choice) {
        case "1"://Enter lab credentials
          labUsr = serverUserCredentials();
          break;
        case "2"://Pseudoterminal, run commands on lab
          if (labUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            pseudoTerminal(labUsr);
          }
          break;
        case "3"://Check connection
          checkConnection(labUsr);
          break;
        case "4"://Return to main menu
          break labLoop;
        case "5":
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
