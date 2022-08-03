package org.patchmanager.apiutils;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DotEnvUser {
  static final Logger LOGGER = LogManager.getLogger(DotEnvUser.class);
  static Dotenv dotenv = null;
  public static String email = "";
  public static String api = "";
  public static String labpassword = "";
  public static String zitsvypassword = "";
  public DotEnvUser() {

    try {
      LOGGER.debug("Loading dotenv file");
      //Dotenv loads the environment variables that keep api key and email
      dotenv = Dotenv.load();
    } catch (Exception e) {
      LOGGER.fatal(".env file cannot be loaded", e);
      System.exit(-1);
    }
    LOGGER.debug("Getting email and API key");
    email = dotenv.get("EMAIL");
    api = dotenv.get("API_KEY");
    labpassword = dotenv.get("LAB_PASSWORD");
    zitsvypassword = dotenv.get("ZITSVY_PASSWORD");
  }
}
