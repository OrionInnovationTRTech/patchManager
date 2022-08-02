package org.patchmanager.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecideFileName {
  static final Logger LOGGER = LogManager.getLogger(DecideFileName.class);

  public static String fileNameDecider(String patch, String versionInput) {
    LOGGER.debug("Defining the fileName");
    StringBuilder fileName = new StringBuilder();
    fileName.append("KANDYLINK_");
    fileName.append(versionInput);
    fileName.append("_P_");
    fileName.append(patch);
    fileName.append("_admin.txt");
    return fileName.toString();
  }
}
