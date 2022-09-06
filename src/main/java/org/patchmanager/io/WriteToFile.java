package org.patchmanager.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteToFile {
  private WriteToFile(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(WriteToFile.class);

  public static void writeToFile(String content, String fileName) {
    LOGGER.info("Writing to file");
    try (BufferedWriter w = new BufferedWriter(new FileWriter(fileName, false))) {
      w.write(content);
      Path path = Paths.get(fileName);
      LOGGER.info("Contents are successfully written into {}", path.toAbsolutePath());

    } catch (IOException e) {
      LOGGER.fatal("IOException while trying to write the content to file");
      System.exit(-1);
    }
  }
}
