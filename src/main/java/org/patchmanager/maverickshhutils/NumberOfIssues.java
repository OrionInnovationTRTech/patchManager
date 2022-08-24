package org.patchmanager.maverickshhutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.apiutils.HttpRequestAndResponse;

import java.util.Scanner;

import static org.patchmanager.apiutils.AuthChecker.checkAuth;

public class NumberOfIssues {
  static Logger LOGGER = LogManager.getLogger(org.patchmanager.services.PrintJiraIssuesToTxtFile.class);
  public static int numberOfIssues(String labelInput){

    LOGGER.info("Jira Issue to Text Service Starting");
    LOGGER.info("Trying to authorize");
    DotEnvUser dotEnvUserObj = new DotEnvUser();

    if (!checkAuth(dotEnvUserObj.email, dotEnvUserObj.api)) {
      LOGGER.fatal("Couldn't authorize, check credentials");
      System.exit(-1);
    }

    LOGGER.info("Trying to send a Http Request to the API and get a response");
    HttpRequestAndResponse httpRequestAndResponse = new HttpRequestAndResponse(labelInput);
    int numberOfIssues = 0;
    JSONObject obj = new JSONObject(httpRequestAndResponse.response.body());
    // total number of issues
    numberOfIssues = obj.getInt("total");
    if (numberOfIssues != 0) {
      return numberOfIssues;
    } else {
      LOGGER.fatal("No issues found with that label");
      System.exit(-1);
    }
    return  0;
  }
}
