package org.patchmanager.api_related;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseJiraIssues {
    /**
     * Returns all issues in ABE-123 Description form line by line
     * @param responseBody takes the response body after GET for issues with label
     * @return string value
     */
    public static String parseJiraIssues(String responseBody){
        JSONObject obj = new JSONObject(responseBody);

        // total number of issues
        int total = obj.getInt("total");

        JSONArray issues = obj.getJSONArray("issues");
        StringBuilder issuesToBeWritten = new StringBuilder();


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
