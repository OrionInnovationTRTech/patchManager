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

public class HttpRequestAndResponse {
    public HttpRequest request;
    public HttpResponse<String> response;

    public HttpRequestAndResponse(String labelInput){
        try {
            request = HttpRequest.newBuilder()
                    //put %20 instead of spaces to resolve illegal character
                    .uri(new URI("https://kandyio.atlassian.net/rest/api/2/search?fields=summary&jql=labels%20%3D%20"+labelInput+"%20ORDER%20BY%20key%20ASC"))
                    //value is base64 encoding of "email:API_KEY"
                    .header("Authorization", encodeBase64(DotEnvUser.email, DotEnvUser.api))
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();

            response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException e) {
            System.out.println("Problem with URI while defining HTTP request and response");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("IOException while defining HTTP request and response");
            System.exit(-1);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while defining HTTP request and response");
            System.exit(-1);
        }

    }
}
