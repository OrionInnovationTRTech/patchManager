package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.LabMenu.labMenu;
import static org.patchmanager.menu.ZitsvyMenu.zitsvyMenu;
import static org.patchmanager.services.PrintJiraIssuesToTxtFile.printJiraIssuesToTxtFile;

public class MainMenu {
  Logger LOGGER = LogManager.getLogger(MainMenu.class);
  static String[] mainMenuItems = {"1. Print Issues to Txt from Jira"
      , "2. Services that uses the lab"
      ,"3. Services that uses the zitsvy server"
      ,"4. Exit"};

  static String[] labMenuItems = {"1. Enter lab credentials"
      ,"2. Pseudo terminal"
      ,"3. File transfer"
      ,"4. Return to the main menu"
      ,"5. Exit program"};

  static String[] zitsvyMenuItems = {"1. Enter zitsvy credentials"
      ,"2. Pseudo terminal"
      ,"3. File transfer"
      ,"4. Maven build in cdwae"
      ,"5. Maven Build in gitwaeall"
      ,"6. Patch Creation"
      ,"7. Maven build and Patch Creation sequentially"
      ,"8. Return to the main menu"
      ,"9. Exit program"};

  static ServerUser labUsr = null;
  static ServerUser zitsvyUsr = null;
  static Scanner scanner = new Scanner(System.in);
  public static void mainMenu() throws IOException, SshException {
    int choice;
    mainLoop: while(true){
      displayMenu("Main Menu", mainMenuItems);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextInt();

      switch (choice) {
        case 1:
          printJiraIssuesToTxtFile();
          break;
        case 2:
          labMenu();
          break;
        case 3:
          zitsvyMenu();
          break;
        case 4:
          System.out.println("Terminating");
          break mainLoop;
        default:
          System.out.println("Invalid menu choice; try again.");
      }
    }

    }
  }

  /*
    1. Print issues to txt from Jira
    2. Services with labs
      1. Pseudo terminal
      2. File transfer
      3. Return to main menu
      4. Exit program
    3. Services with zitsvy
      1. Maven build in cdwae
      2. Maven Build in gitwaeall
        take checkout branch, label, version, patch no, take fix number from jira
      3. FC Creation
      4. Patch Creation
      5. Maven build and Patch Creation sequentially
      6. File transfer
      7. Pseudo terminal
      8. Exit to main menu
      9. Exit program
     */
