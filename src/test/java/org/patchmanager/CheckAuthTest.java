package org.patchmanager;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.patchmanager.api_related.DotEnvUser;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.patchmanager.api_related.AuthChecker.checkAuth;
import static org.patchmanager.api_related.DotEnvUser.api;
import static org.patchmanager.api_related.DotEnvUser.email;

public class CheckAuthTest {
    @Test
    public void checkAuthTest() throws ParseException, URISyntaxException, IOException, InterruptedException {
        DotEnvUser dotEnvUserObj = new DotEnvUser();
        assertTrue(checkAuth(email, api));
    }
}
