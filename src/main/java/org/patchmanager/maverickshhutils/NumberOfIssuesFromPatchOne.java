package org.patchmanager.maverickshhutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.apiutils.HttpRequestAndResponse;

import static org.patchmanager.apiutils.AuthChecker.checkAuth;

public class NumberOfIssuesFromPatchOne {
  static Logger LOGGER = LogManager.getLogger(NumberOfIssuesFromPatchOne.class);
  public static int numberOfIssuesStartingPatchOne(String labelInput, String patch){

    LOGGER.info("Jira Issue to Text Service Starting");
    LOGGER.info("Trying to authorize");
    DotEnvUser dotEnvUserObj = new DotEnvUser();

    if (!checkAuth(dotEnvUserObj.email, dotEnvUserObj.api)) {
      LOGGER.fatal("Couldn't authorize, check credentials");
      System.exit(-1);
    }
    int numberofIssuesSumOfPatches = 0;
    int numberOfIssuesForPatch = 0;

    //get the last index of patch in label assuming it is the patch
    //if it is not 1 decrease it by 1 in a for loop to get all the issues
    //this is finally going to be used to increase the load number in pom.xml

    int lastIndexOfPatchInLabel = labelInput.lastIndexOf(patch);

    for (int i = Integer.parseInt(patch); i > 0; i--) {
      LOGGER.info("Trying to send a Http Request to the API and get a response");
      HttpRequestAndResponse httpRequestAndResponse = new HttpRequestAndResponse(labelInput);
      JSONObject obj = new JSONObject(httpRequestAndResponse.response.body());
      // total number of issues
      numberOfIssuesForPatch = obj.getInt("total");
      if (numberOfIssuesForPatch != 0) {
        LOGGER.info("Found " + numberOfIssuesForPatch + "issues with label " + labelInput);
        numberofIssuesSumOfPatches += numberOfIssuesForPatch;
      } else {
        LOGGER.fatal("No issues found with that label");
      }
      StringBuilder newLabelInput = new StringBuilder();
      newLabelInput.append(labelInput, 0, lastIndexOfPatchInLabel);
      newLabelInput.append(i);
      newLabelInput.append(labelInput.substring(lastIndexOfPatchInLabel + String.valueOf(i).length()));
      labelInput = newLabelInput.toString();
    }
    return numberofIssuesSumOfPatches;
  }
}
