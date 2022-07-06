package org.patchmanager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;


import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {
    //Writes to File
    //TODO Add parameter for filename
    static void writeToFile(String content, String fileName) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,false));
            writer.write(content);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //increasing the major part of the version by 5 e.g. from 4.8.1 to 9.8.1
    public static String increaseMajorNoOfVersion(String version) {
        String[] splittedVersionWithDot = version.split("\\.");
        String versionMajorNumberPlusFive = String.valueOf(Integer.parseInt(splittedVersionWithDot[0]) + 5);
        splittedVersionWithDot[0] = versionMajorNumberPlusFive;

        /*String newVersion = versionMajorNumberPlusFive +
                "." +
                splittedVersionWithDot[1] +
                "." +
                splittedVersionWithDot[2];*/
        return String.join(".", splittedVersionWithDot);

    }
    public static String encodeBase64(String email, String api) {
        String wantedPlainString = email + ":" + api;
        String encodedString = Base64.getEncoder().encodeToString(wantedPlainString.getBytes());
        String wantedFinalString = "Basic "+ encodedString;
        return wantedFinalString;
    }
    public static String fileNameDecider(String labelInput) {
        String[] splittedLabelWithUnderline = labelInput.split("_");
        String version = splittedLabelWithUnderline[1];
        String patch = splittedLabelWithUnderline[3];
        //increasing major e.g x.1.1 part of the version with 5 so it is x+5.1.1
        String versionPlusFive = increaseMajorNoOfVersion(version);

        StringBuilder fileName = new StringBuilder();
        fileName.append("KANDYLINK_");
        fileName.append(versionPlusFive);
        fileName.append(".dl35_P_");
        fileName.append(patch);
        fileName.append("_admin.txt");
        return fileName.toString();
    }
    public static String writeOutro(String labelInput) {

        String[] splittedLabelWithUnderline = labelInput.split("_");
        String version = splittedLabelWithUnderline[1];
        String patch = splittedLabelWithUnderline[3];
        //increasing major e.g x.1.1 part of the version with 5 so it is x+5.1.1
        String versionPlusFive = increaseMajorNoOfVersion(version);

        String outro = "\n" +
                "END DETAILED_DESCRIPTION\n" +
                "MD5SUM (SPiDR_" + versionPlusFive + ".dl35_P_" + patch + ".tar.gz) : afa55f78898ca4d3171e5df2e44f08c5\n\n";
        //writeToFile(outro, fileNameDecider(labelInput));
        return outro;
    }
    public static String writeIntro(String labelInput) {

        String[] splittedLabelWithUnderline = labelInput.split("_");
        String version = splittedLabelWithUnderline[1];
        String patch = splittedLabelWithUnderline[3];
        Date date = new Date();
        SimpleDateFormat SimpDateFormatObj = new SimpleDateFormat("yyyyMMdd");
        String strDate= SimpDateFormatObj.format(date);

        String newVersion = increaseMajorNoOfVersion(version);

        StringBuilder intro = new StringBuilder();
        intro.append("PRODUCT_LINE: KANDYLINK\n");
        intro.append("============================================\n\n");
        intro.append("CATEGORY: GEN\n");
        intro.append("PREREQUISITES: \n");
        intro.append("END\n");
        intro.append("PATCH ID: KANDYLINK_").append(newVersion).append(".dl35_P_").append(patch).append("\n");
        intro.append("LOADS: KANDYLINK_").append(newVersion).append("\n");
        intro.append("END\n");
        intro.append("STATUS: V\n");
        intro.append("WEB_POST: Y\n");
        intro.append("STATUS DATE: ").append(strDate).append("\n");
        intro.append("TITLE: Patch ").append(patch).append("\n");
        intro.append("DETAILED_DESCRIPTION:\n");
        intro.append("This patch includes all fix that were previously released in earlier patches and therefore only the latest patches needs to be applied. \n");
        intro.append("For a complete list of fixes in this patch please refer to the individual patch admin files from the previously released patches.\n");
        intro.append("Please check to KANDYLINK ").append(version).append(" Patch ").append(patch).append(" Release Notes for details.\n\n");
        intro.append("Includes fixes for following issues:\n");
        //writeToFile(intro.toString(), fileNameDecider(labelInput));
        return intro.toString();
    }
    public static String parse(String responseBody){
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
        //writeToFile(issuesToBeWritten.toString(), fileNameDecider(labelInput));
        return issuesToBeWritten.toString();
    }
    public static void main(String[] args) {
        try{
            String labelInput = "KL_4.8.1_P_4";
            String email = "atokgoz@avctechnologies.com";
            String api = "p43jlmcpVeQwknl0klEUA995";
            String fileName = fileNameDecider(labelInput);
            HttpRequest request = HttpRequest.newBuilder()
                    //put %20 instead of spaces to resolve illegal character
                    .uri(new URI("https://kandyio.atlassian.net/rest/api/2/search?fields=summary&jql=labels%20%3D%20"+labelInput+"%20ORDER%20BY%20key%20ASC"))
                    //value is base64 encoding of "email:API_KEY"
                    .header("Authorization", encodeBase64(email,api))
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            StringBuilder finalStr = new StringBuilder();
            finalStr.append(writeIntro(labelInput)).append(parse(response.body())).append(writeOutro(labelInput));
            writeToFile(finalStr.toString(), fileName);

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}