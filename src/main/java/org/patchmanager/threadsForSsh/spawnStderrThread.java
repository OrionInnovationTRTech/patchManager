package org.patchmanager.threadsForSsh;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class spawnStderrThread implements Runnable{
  InputStream errStream;
  public spawnStderrThread(InputStream errStream){this.errStream = errStream;}
  @Override
  public void run() {
    while (true){
      try {
        if (nonNull(errStream)) {
          Thread.sleep(2000);
          System.out.println("Error stream reads: ");
          System.out.println(Arrays.toString(errStream.readAllBytes()));
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
