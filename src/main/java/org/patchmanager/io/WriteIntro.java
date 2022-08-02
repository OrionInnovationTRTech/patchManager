package org.patchmanager.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WriteIntro {
  static Logger LOGGER = LogManager.getLogger(WriteIntro.class);

  public static String writeIntro(String patch, String versionLower, String versionHigher, String versionInput, String strDate) {
    LOGGER.debug("Creating the string for the intro(until issues) part");
    StringBuilder intro = new StringBuilder();
    intro.append("PRODUCT_LINE: KANDYLINK\n");
    intro.append("============================================\n\n");
    intro.append("CATEGORY: GEN\n");
    intro.append("PREREQUISITES: \n");
    intro.append("END\n");
    intro.append("PATCH ID: KANDYLINK_").append(versionInput).append("_P_").append(patch).append("\n");
    intro.append("LOADS: KANDYLINK_").append(versionHigher).append("\n");
    intro.append("END\n");
    intro.append("STATUS: V\n");
    intro.append("WEB_POST: Y\n");
    intro.append("STATUS DATE: ").append(strDate).append("\n");
    intro.append("TITLE: Patch ").append(patch).append("\n");
    intro.append("DETAILED_DESCRIPTION:\n");
    intro.append("This patch includes all fix that were previously released in earlier patches and therefore only the latest patches needs to be applied. \n");
    intro.append("For a complete list of fixes in this patch please refer to the individual patch admin files from the previously released patches.\n");
    intro.append("Please check to KANDYLINK ").append(versionLower).append(" Patch ").append(patch).append(" Release Notes for details.\n\n");
    intro.append("Includes fixes for following issues:\n"); //This line is not in parseJiraIssues method to check if parse returns empty string
    return intro.toString();
  }
}
