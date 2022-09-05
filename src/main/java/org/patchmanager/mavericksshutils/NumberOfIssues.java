package org.patchmanager.mavericksshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.apiutils.HttpRequestAndResponse;



import static org.patchmanager.apiutils.AuthChecker.checkAuth;

public class NumberOfIssues {
  static Logger LOGGER = LogManager.getLogger(NumberOfIssues.class);
  public static int numberOfIssues(String labelInput){

    LOGGER.info("Jira Issue to Text Service Starting");
    LOGGER.info("Trying to authorize");
    DotEnvUser dotEnvUserObj = new DotEnvUser();

    if (!checkAuth(dotEnvUserObj.email, dotEnvUserObj.api)) {
      LOGGER.fatal("Couldn't authorize, check credentials");
      System.exit(-1);
    }
    int numberOfIssuesForPatch = 0;
    LOGGER.info("Trying to send a Http Request to the API and get a response");
    HttpRequestAndResponse httpRequestAndResponse = new HttpRequestAndResponse(labelInput);
    JSONObject obj = new JSONObject(httpRequestAndResponse.response.body());
    // total number of issues
    numberOfIssuesForPatch = obj.getInt("total");
    if (numberOfIssuesForPatch != 0) {
      LOGGER.info("Found " + numberOfIssuesForPatch + " issues with label " + labelInput);
    } else {
      LOGGER.fatal("No issues found with that label");
    }

    return numberOfIssuesForPatch;
  }
}
