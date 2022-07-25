package org.patchmanager.api_related;

import java.util.Base64;

public class EncodeBase64 {
    public static String encodeBase64(String email, String api) {
        String wantedPlainString = email + ":" + api;
        String encodedString = Base64.getEncoder().encodeToString(wantedPlainString.getBytes());
        String wantedFinalString = "Basic "+ encodedString;
        return wantedFinalString;
    }
}
