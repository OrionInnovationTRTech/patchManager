package org.patchmanager.writing_to_file;

public class WriteOutro {
    public static String writeOutro(String patch, String versionHigher) {
        String outro = "\n" +
                "END DETAILED_DESCRIPTION\n" +
                "KANDYLINK_" + versionHigher + "_P_" + patch + ".tar.gz\n\n";
        return outro;
    }
}
