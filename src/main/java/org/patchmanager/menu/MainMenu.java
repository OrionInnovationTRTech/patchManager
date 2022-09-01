package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.LabMenu.labMenu;
import static org.patchmanager.menu.ZitsvyMenu.zitsvyMenu;
import static org.patchmanager.services.PrintJiraIssuesToTxtFile.printJiraIssuesToTxtFile;
import static org.patchmanager.services.TestNewService.testNewService;

public class MainMenu {
  Logger LOGGER = LogManager.getLogger(MainMenu.class);
  static String[] mainMenuItems = {"1. Print Issues to Txt from Jira"
      , "2. Services that uses the lab"
      ,"3. Services that uses the zitsvy server"
      ,"4. Testing service"
      ,"5. Exit"};

  static String[] labMenuItems = {"1. Enter lab credentials"
      ,"2. Pseudo terminal"
      ,"3. Check connection"
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
    String choice;
    mainLoop: while(true){
      displayMenu("Main Menu", mainMenuItems);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextLine();
      switch (choice) {
        case "1":
          printJiraIssuesToTxtFile();
          break;
        case "2":
          labMenu();
          break;
        case "3":
          zitsvyMenu();
          break;
        case "4":
          ServerUser zitsvyUser = new ServerUser("senas", "10.254.51.215", "KandyAVCT1");
          ServerUser labUser = new ServerUser("ntsysadm", "47.168.150.36", "RAPtor1234");
          testNewService(zitsvyUser);
          break;
        case "5":
          System.out.println("Terminating");
          break mainLoop;
        default:
          System.out.println("Invalid menu choice, try again.");
      }
    }

    }
  }
