package org.patchmanager;

import org.junit.jupiter.api.Test;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.patchmanager.apiutils.AuthChecker.checkAuth;
import static org.patchmanager.apiutils.DotEnvUser.api;
import static org.patchmanager.apiutils.DotEnvUser.email;

public class CheckAuthTest {
  @Test
  public void checkAuthTest() throws URISyntaxException, IOException, InterruptedException {
    DotEnvUser dotEnvUserObj = new DotEnvUser();
    assertTrue(checkAuth(email, api));
  }
}
