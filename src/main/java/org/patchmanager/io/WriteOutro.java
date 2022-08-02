package org.patchmanager.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WriteOutro {
  static Logger LOGGER = LogManager.getLogger(WriteOutro.class);

  public static String writeOutro(String patch, String versionHigher) {
    LOGGER.debug("Creating the string for the outro(after issues) part");
    String outro = "\n" +
        "END DETAILED_DESCRIPTION\n" +
        "KANDYLINK_" + versionHigher + "_P_" + patch + ".tar.gz\n\n";
    return outro;
  }
}
