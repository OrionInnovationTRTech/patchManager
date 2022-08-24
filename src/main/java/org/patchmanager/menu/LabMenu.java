package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;

import java.io.IOException;

import static org.patchmanager.maverickshhutils.PseudoTerminal.pseudoTerminal;
import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.MainMenu.*;
import static org.patchmanager.services.FileTransfer.fileTransfer;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class LabMenu {
  public static void labMenu() throws IOException, SshException {
    int choice;
    labLoop : while(true) {
      displayMenu("Lab Services Menu", labMenuItems);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextInt();

      switch (choice) {
        case 1://Enter lab credentials
          labUsr = serverUserCredentials();
          break;
        case 2://Pseudoterminal
          if (labUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            pseudoTerminal(labUsr);
          }
          break;
        case 3://File transfer
          if (labUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            fileTransfer(labUsr);
          }
          break;
        case 4://Return to main menu
          break labLoop;
        case 5:
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
