package org.patchmanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.NamingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {
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
    public static String encodeBase64(String email, String api) {
        String wantedPlainString = email + ":" + api;
        String encodedString = Base64.getEncoder().encodeToString(wantedPlainString.getBytes());
        String wantedFinalString = "Basic "+ encodedString;
        return wantedFinalString;
    }
    public static String fileNameDecider(String patch, String versionInput) {
        StringBuilder fileName = new StringBuilder();
        fileName.append("KANDYLINK_");
        fileName.append(versionInput);
        fileName.append("_P_");
        fileName.append(patch);
        fileName.append("_admin.txt");
        return fileName.toString();
    }

    public static String writeIntro(String patch, String versionLower, String versionHigher, String versionInput, String strDate) {

        StringBuilder intro = new StringBuilder();
        intro.append("PRODUCT_LINE: KANDYLINK\n");
        intro.append("============================================\n\n");
        intro.append("CATEGORY: GEN\n");
        intro.append("PREREQUISITES: \n");
        intro.append("END\n");
        intro.append("PATCH ID: KANDYLINK_").append(versionInput).append("_P_").append(patch).append("\n");
        intro.append("LOADS: KANDYLINK_").append(versionHigher).append("\n");
        intro.append("END\n");
        intro.append("STATUS: V\n");
        intro.append("WEB_POST: Y\n");
        intro.append("STATUS DATE: ").append(strDate).append("\n");
        intro.append("TITLE: Patch ").append(patch).append("\n");
        intro.append("DETAILED_DESCRIPTION:\n");
        intro.append("This patch includes all fix that were previously released in earlier patches and therefore only the latest patches needs to be applied. \n");
        intro.append("For a complete list of fixes in this patch please refer to the individual patch admin files from the previously released patches.\n");
        intro.append("Please check to KANDYLINK ").append(versionLower).append(" Patch ").append(patch).append(" Release Notes for details.\n\n");
        intro.append("Includes fixes for following issues:\n"); //This line is not in parseJiraIssues method to check if parse returns empty string
        return intro.toString();
    }

    /**
     * Returns all issues in ABE-123 Description form line by line
     * @param responseBody takes the response body after GET for issues with label
     * @return string value
     */
    private static String parseJiraIssues(String responseBody){
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
    public static String writeOutro(String patch, String versionHigher) {
        String outro = "\n" +
                "END DETAILED_DESCRIPTION\n" +
                "KANDYLINK_" + versionHigher + "_P_" + patch + ".tar.gz\n\n";
        return outro;
    }
    private static final Option LABEL = new Option("l", "label", true, "Specify the label e.g. KL_4.8.1_P_4");
    private static final Option VERSION = new Option("v", "version", true, "Specify the version in the format of number.number.number.charcharnumber e.g. 9.8.1.dl35" );
    private static final Option PATCH = new Option("p", "patch", true, "Specify the patch as a number e.g. 14");
    private static void printHelpCmd(Options options){
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("PackageManager");

        formatter.printUsage(pw, 100, "java -jar patchManager.jar -l label -v version -p patchNo");
        formatter.printOptions(pw, 100,options,2,5);
        pw.close();
    }
    public static void main(String[] args) {
        try {


            String labelInput = "";
            String versionInput = "";
            String patchInput = "";

            CommandLineParser commandLineParser = new DefaultParser();
            Options options = new Options();
            options.addOption(LABEL);
            options.addOption(VERSION);
            options.addOption(PATCH);

            try {
                CommandLine commandLine = commandLineParser.parse(options, args);


                try {
                    missingOptionChecker(options, commandLine);
                } catch (Exception e) {
                    throw new IllegalArgumentException("There is a missing option");
                }


                labelInput = commandLine.getOptionValue(LABEL);

                //Regex is in the form of number . number . number . twoChars number
                Pattern patternVersion = Pattern.compile("\\d+.\\d+.\\d+.[a-zA-Z]{2}\\d+");
                Matcher matcherVersion = patternVersion.matcher(commandLine.getOptionValue(VERSION));
                if (commandLine.hasOption(VERSION.getOpt()) && matcherVersion.matches()) {
                    versionInput = commandLine.getOptionValue(VERSION);
                } else {
                    System.out.println("Version does not fit the criteria");
                    printHelpCmd(options);
                    System.exit(-1);
                }

                //Regex is in the form of number
                Pattern patternPatch = Pattern.compile("\\d+");
                Matcher matcherPatch = patternPatch.matcher(commandLine.getOptionValue(PATCH));
                if (matcherPatch.matches()) {
                    patchInput = commandLine.getOptionValue(PATCH);
                } else {
                    System.out.println("Patch does not fit the criteria");
                    printHelpCmd(options);
                    System.exit(-1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            Dotenv dotenv = null;
            try {
                //Dotenv loads the environment variables that keep api key and email
                dotenv = Dotenv.load();
            } catch (Exception e) {
                System.out.println(".env file cannot be loaded");
                throw new RuntimeException(e);
            }

            String email = dotenv.get("EMAIL");
            String api = dotenv.get("API_KEY");

            if (!checkAuth(email, api)) {
                System.out.println("Couldn't authorize, check credentials");
                System.exit(-1);
            }

            //There are 2 versions that are used, a higher one like 9.8.1 and a lower one like 4.8.1
            //Split by numbers
            String[] splitVersionByNumbers = versionInput.split("(\\D+)");
            String versionHigher = String.join(".", splitVersionByNumbers[0], splitVersionByNumbers[1], splitVersionByNumbers[2]);
            String lowerFirstNoByFive = String.valueOf(Integer.parseInt(splitVersionByNumbers[0]) - 5);
            String versionLower = String.join(".", lowerFirstNoByFive, splitVersionByNumbers[1], splitVersionByNumbers[2]);

            Date date = new Date();
            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat("yyyyMMdd");
            String strDate= simpleDateFormatObj.format(date);

            String fileName = fileNameDecider(patchInput, versionInput);


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


            if (!parseJiraIssues(response.body()).isEmpty()) {
                StringBuilder finalStr = new StringBuilder();
                finalStr.append(writeIntro( patchInput, versionLower, versionHigher, versionInput, strDate))
                        .append(parseJiraIssues(response.body()))
                        .append(writeOutro( patchInput, versionHigher));
                writeToFile(finalStr.toString(), fileName);
            } else {
                System.out.println("No issues found with that label");
                System.exit(-1);
            }

            /*String fileContent = txtFileRead("template.txt");
            fileContent = txtContentReplace(versionLower, patchInput, versionHigher, strDate, response, fileContent);
            writeToFile(fileContent, "template.txt");*/

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void missingOptionChecker(Options options, CommandLine commandLine){


            if (!commandLine.hasOption(PATCH.getOpt()) || !commandLine.hasOption(VERSION.getOpt()) || !commandLine.hasOption(LABEL.getOpt())) {
                System.out.println("There is a missing option");
                printHelpCmd(options);
                throw new IllegalArgumentException("There is a missing option");
            }

    }

    /**
     * Checks authorization
     * @param email email in .env
     * @param api api in .env
     * @return true if atlassian returns 200 success
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private static boolean checkAuth(String email, String api) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest requestAuth = HttpRequest.newBuilder()
                .uri(new URI("https://kandyio.atlassian.net/rest/auth/1/session"))
                //value is base64 encoding of "email:API_KEY"
                .header("Authorization", encodeBase64(email, api))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        HttpResponse<String> responseAuth = HttpClient.newBuilder()
                .build()
                .send(requestAuth, HttpResponse.BodyHandlers.ofString());
        return (responseAuth.statusCode() == 200);
    }

    @NotNull
    private static String txtContentReplace(String versionLower, String patch, String versionHigher, String strDate, HttpResponse<String> response, String fileContent) {
        fileContent = fileContent.replace("{INCREASED_VERSION}", versionHigher);
        fileContent = fileContent.replace("{DATE}", strDate);
        fileContent = fileContent.replace("{VERSION}", versionLower);
        fileContent = fileContent.replace("{ISSUES}", parseJiraIssues(response.body()));
        fileContent = fileContent.replace("{PATCH}", patch);
        return fileContent;
    }

    private static String txtFileRead(String templateFileName) throws IOException {
        Path filePath = Path.of(templateFileName);
        String fileContent = Files.readString(filePath);
        return fileContent;
    }
}