package org.patchmanager.menu;

public class DisplayMenu {
  public static void displayMenu(String title, String[] menuItems) {
    System.out.println();
    System.out.println("P A T C H   M A N A G E R");
    System.out.println(title.toUpperCase());
    System.out.println("==========================");
    for (int i = 0; i < menuItems.length; i++) {
      System.out.println(menuItems[i]);
    }
    System.out.println("==========================");
  }
}
