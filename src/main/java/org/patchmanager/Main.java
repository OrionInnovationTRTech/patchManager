package org.patchmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import static org.patchmanager.menu.MainMenu.mainMenu;


/**
 * Main class that links all the methods.
 */
public class Main {
  /**
   * Main method that runs the patchManager.
   *
   * @param args cli args, they are supposed to be
   *             --version --label --patch or their short versions
   */
  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    Logger LOGGER = null;
    new DotEnvUser();
    try {
      LOGGER = LogManager.getLogger(Main.class);
    } catch (Exception e) {
      System.out.println("log4j2.xml cannot be found");
      System.exit(-1);
    }
    mainMenu();
    LOGGER.info("Finishing the main function");
    long finish = System.currentTimeMillis();
    double timeElapsed = finish - start;
    System.out.println(timeElapsed/1000);
    System.out.println("seconds passed");
  }
}