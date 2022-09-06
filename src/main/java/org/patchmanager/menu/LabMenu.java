package org.patchmanager.menu;
import java.io.IOException;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.MainMenu.*;
import static org.patchmanager.services.PseudoTerminal.pseudoTerminal;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;

public class LabMenu {
  private LabMenu(){
    throw new IllegalStateException("Utility class");
  }
  public static void labMenu() throws IOException {
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
        case "3"://Return to main menu
          break labLoop;
        case "4":
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
