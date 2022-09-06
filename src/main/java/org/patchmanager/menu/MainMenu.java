package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.mavericksshutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.LabMenu.labMenu;
import static org.patchmanager.menu.ZitsvyMenu.zitsvyMenu;
import static org.patchmanager.services.PrintJiraIssuesToTxtFile.printJiraIssuesToTxtFile;

public class MainMenu {
  Logger LOGGER = LogManager.getLogger(MainMenu.class);
  static final String[] MAIN_MENU_ITEMS = {"1. Creation of admin.txt file"
      , "2. Go to zitsvy menu"
      , "3. Run commands in lab"
      , "4. Exit"};

  static final String[] LAB_MENU_ITEMS = {"1. Enter lab credentials"
      ,"2. Run command on lab server"
      ,"3. Return to the main menu"
      ,"4. Exit program"};

  static final String[] ZITSVY_MENU_ITEMS = {"1. Enter zitsvy credentials"
      ,"2. Run command on zitsvy server"
      ,"3. File transfer"
      ,"4. Maven Build in gitwaeall"
      ,"5. FC Creation"
      ,"6. Patch Creation"
      ,"7. Return to the main menu"
      ,"8. Exit program"};
  static ServerUser labUsr = null;
  static ServerUser zitsvyUsr = null;
  static Scanner scanner = new Scanner(System.in);
  public static void mainMenu() throws IOException, SshException {
    String choice;
    mainLoop: while(true){
      displayMenu("Main Menu", MAIN_MENU_ITEMS);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextLine();
      switch (choice) {
        case "1":
          printJiraIssuesToTxtFile();
          break;
        case "2":
          zitsvyMenu();
          break;
        case "3":
          labMenu();
          break;
        case "4":
          System.out.println("Terminating");
          break mainLoop;
        default:
          System.out.println("Invalid menu choice, try again.");
      }
    }

    }
  }