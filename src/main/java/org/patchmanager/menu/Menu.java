package org.patchmanager.menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class Menu {
  Logger LOGGER = LogManager.getLogger(Menu.class);
  public static void displayMenu(String title, String[] menuItems) {
    System.out.println("P A T C H   M A N A G E R");
    System.out.println(title.toUpperCase());
    System.out.println("==========================");
    for (int i = 0; i < menuItems.length; i++) {
      System.out.println(menuItems[i]);
    }
    System.out.println("==========================");
    System.out.print("Enter your choice: ");
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
      1. Maven Build
        take checkout branch, label, version, patch no, take fix number from jira
      2. FC Creation
      3. Patch Creation
      4. Maven build and FC Creation sequentially
      5. Maven build, FC Creation and Patch Creation sequentially
      6. File transfer
      7. Pseudo terminal
      8. Exit to main menu
      9. Exit program
     */



}
