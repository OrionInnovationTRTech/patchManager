package org.patchmanager.api_related;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvUser {

    static Dotenv dotenv = null;
    public static String email = "";
    public static String api = "";
    public DotEnvUser(){
        try {
            //Dotenv loads the environment variables that keep api key and email
            dotenv = Dotenv.load();
        } catch (Exception e) {
            System.out.println(".env file cannot be loaded");
            System.exit(-1);
        }
        email = dotenv.get("EMAIL");
        api = dotenv.get("API_KEY");
    }
}
