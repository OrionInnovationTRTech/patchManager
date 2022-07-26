package org.patchmanager.api_related;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DotEnvUser {
    static final Logger LOGGER  = LogManager.getLogger(DotEnvUser.class);
    static Dotenv dotenv = null;
    public static String email = "";
    public static String api = "";
    public DotEnvUser(){

        try {
            LOGGER.debug("Loading dotenv file");
            //Dotenv loads the environment variables that keep api key and email
            dotenv = Dotenv.load();
        } catch (Exception e) {
            LOGGER.fatal(".env file cannot be loaded");
            System.exit(-1);
        }
        LOGGER.debug("Getting email and API key");
        email = dotenv.get("EMAIL");
        api = dotenv.get("API_KEY");
    }
}
