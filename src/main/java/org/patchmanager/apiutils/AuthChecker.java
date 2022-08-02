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

/**
 *
 */
public class AuthChecker {

  static final Logger LOGGER = LogManager.getLogger(AuthChecker.class);

  /**
   * Checks authorization
   *
   * @param email email in .env
   * @param api   api in .env
   * @return true if atlassian returns 200 success
   */
  public static boolean checkAuth(String email, String api) {
    try {
      LOGGER.debug("Sending a Http request for authorization");
      HttpRequest requestAuth = HttpRequest.newBuilder().uri(new URI("https://kandyio.atlassian.net/rest/auth/1/session"))
          //value is base64 encoding of "email:API_KEY"
          .header("Authorization", encodeBase64(email, api)).timeout(Duration.of(10, SECONDS)).GET().build();

      LOGGER.debug("Getting a Http response for authorization");
      HttpResponse<String> responseAuth = HttpClient.newBuilder().build().send(requestAuth, HttpResponse.BodyHandlers.ofString());
      return (responseAuth.statusCode() == 200);
    } catch (URISyntaxException e) {
      LOGGER.fatal("Problem with URI while checking for authorization");
      System.exit(-1);
    } catch (IOException e) {
      LOGGER.fatal("IOException while checking for authorization");
      System.exit(-1);
    } catch (InterruptedException e) {
      LOGGER.fatal("InterruptedException while checking for authorization");
      System.exit(-1);
    }
    return false;
  }


}
