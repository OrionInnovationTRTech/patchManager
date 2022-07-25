package org.patchmanager.writing_to_file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteToFile {
    public static void writeToFile(String content, String fileName) {
        try(BufferedWriter w = new BufferedWriter(new FileWriter(fileName,false))){
            w.write(content);
            Path path = Paths.get(fileName);
            System.out.println("Content successfully written into " + path.toAbsolutePath());

        }
        catch (IOException e){
            System.out.println("IOException while trying to write the content to file");
            System.exit(-1);
        }
    }
}
