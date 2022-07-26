package org.patchmanager.api_related;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParseJiraIssues {
    static final Logger LOGGER  = LogManager.getLogger(ParseJiraIssues.class);
    /**
     * Returns all issues in ABE-123 Description form line by line
     * @param responseBody takes the response body after GET for issues with label
     * @return string value
     */
    public static String parseJiraIssues(String responseBody){
        LOGGER.debug("Creating a JSON Object with the body of the Http response");
        JSONObject obj = new JSONObject(responseBody);

        // total number of issues
        int total = obj.getInt("total");

        JSONArray issues = obj.getJSONArray("issues");
        StringBuilder issuesToBeWritten = new StringBuilder();

        LOGGER.debug("Creating the string for the issues part");
        for (int i = 0; i < total; i ++)
        {
            //gets the key of the issue e.g. ABE-1234
            issuesToBeWritten.append(issues.getJSONObject(i).getString("key")).append("\n");
            //get the summary of the issue e.g. Rest Api Spec Doc should be updated
            issuesToBeWritten.append(issues.getJSONObject(i).getJSONObject("fields").getString("summary")).append("\n\n");

        }
        return issuesToBeWritten.toString();
    }
}
