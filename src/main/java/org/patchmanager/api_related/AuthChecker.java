package org.patchmanager.api_related;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.patchmanager.api_related.EncodeBase64.encodeBase64;

public class AuthChecker {
    /**
     * Checks authorization
     * @param email email in .env
     * @param api api in .env
     * @return true if atlassian returns 200 success
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean checkAuth(String email, String api){
        try {
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
        } catch (URISyntaxException e) {
            System.out.println("Problem with URI while checking for authorization");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("IOException while checking for authorization");
            System.exit(-1);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while checking for authorization");
            System.exit(-1);
        }
        return false;
    }



}
