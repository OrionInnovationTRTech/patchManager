package org.patchmanager.apiutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.patchmanager.apiutils.EncodeBase64.encodeBase64;

public class HttpRequestAndResponse {
  static final Logger LOGGER = LogManager.getLogger(HttpRequestAndResponse.class);
  public HttpRequest request;
  public HttpResponse<String> response;

  public HttpRequestAndResponse(String labelInput) {
    try {
      LOGGER.debug("Sending a Http request for the issues");
      this.request = createRequest(labelInput);

      LOGGER.debug("Getting a Http response for the issues");
      this.response = createResponse();
    } catch (URISyntaxException e) {
      LOGGER.fatal("Problem with URI while defining HTTP request and response");
      System.exit(-1);
    } catch (IOException e) {
      LOGGER.fatal("IOException while defining HTTP request and response");
      System.exit(-1);
    } catch (InterruptedException e) {
      LOGGER.fatal("InterruptedException while defining HTTP request and response");
      System.exit(-1);
    }

  }

  public HttpResponse<String> createResponse() throws IOException, InterruptedException {
    response = HttpClient.newBuilder()
        .build()
        .send(request, HttpResponse.BodyHandlers.ofString());
    return response;
  }

  public HttpRequest createRequest(String labelInput) throws URISyntaxException {
    request = HttpRequest.newBuilder()
        //put %20 instead of spaces to resolve illegal character
        .uri(new URI("https://kandyio.atlassian.net/rest/api/2/search?fields=summary&jql=labels%20%3D%20" + labelInput + "%20ORDER%20BY%20key%20ASC"))
        //value is base64 encoding of "email:API_KEY"
        .header("Authorization", encodeBase64(DotEnvUser.email, DotEnvUser.api))
        .timeout(Duration.of(10, SECONDS))
        .GET()
        .build();
    return request;
  }
}
