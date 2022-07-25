package org.patchmanager.writing_to_file;

public class DecideFileName {
    public static String fileNameDecider(String patch, String versionInput) {
        StringBuilder fileName = new StringBuilder();
        fileName.append("KANDYLINK_");
        fileName.append(versionInput);
        fileName.append("_P_");
        fileName.append(patch);
        fileName.append("_admin.txt");
        return fileName.toString();
    }
}
