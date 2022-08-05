package org.patchmanager.threadsForSsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class spawnStdoutThread implements Runnable{
  InputStream inputStream;
  public spawnStdoutThread(InputStream inputStream){this.inputStream = inputStream;}
  @Override
  public void run() {
    while (true){
      try {

        if (nonNull(inputStream)) {
          Thread.sleep(2000);
          System.out.println("Output stream reads: ");
          System.out.println(Arrays.toString(inputStream.readAllBytes()));
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
