package org.patchmanager.api_related;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.patchmanager.command_line.OptionsRelated.VERSION;

public class ParseJiraIssues {
    static final Logger LOGGER  = LogManager.getLogger(ParseJiraIssues.class);
    /**
     * Returns all issues in ABE-123 Description form line by line
     * @param responseBody takes the response body after GET for issues with label
     * @return string value
     */
    public static String parseJiraIssues(String responseBody, String versionLower, String patch){
        LOGGER.debug("Creating a JSON Object with the body of the Http response");
        JSONObject obj = new JSONObject(responseBody);

        // total number of issues
        int total = obj.getInt("total");

        JSONArray issues = obj.getJSONArray("issues");
        StringBuilder issuesToBeWritten = new StringBuilder();

        LOGGER.debug("Creating the string for the issues part");

        //|.*Patch_3|.*Patch 3|.*Patch3|.*P3|.*P 3|.*P_3|
        String patchPattern = "|.*Patch_"+patch+"|.*Patch "+patch+"|.*Patch"+patch+"|.*P"+patch+"|.*P "+patch+"|.*P_"+patch+"|";
        String regex = ".*\\[.*\\]"+patchPattern+".*"+versionLower;
        //For removing everything until things in brackets
        //Removing everything until Patch 3 etc.
        //And removing everything until version no e.g. 4.8.1
        for (int i = 0; i < total; i ++)
        {
            //gets the key of the issue e.g. ABE-1234
            issuesToBeWritten.append(issues.getJSONObject(i).getString("key")).append("\n");
            //get the summary of the issue e.g. Rest Api Spec Doc should be updated
            String issueSummary = issues.getJSONObject(i).getJSONObject("fields").getString("summary");
            issueSummary = issueSummary.replaceFirst(regex, "");
            //Trim whitespaces and dashes from the start if there are any
            issueSummary = issueSummary.replaceFirst("^[^\\w]+", "");
            issuesToBeWritten.append(issueSummary).append("\n\n");

        }
        return issuesToBeWritten.toString();
    }
}
