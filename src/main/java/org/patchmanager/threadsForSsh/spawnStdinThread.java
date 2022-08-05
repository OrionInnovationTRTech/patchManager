package org.patchmanager.threadsForSsh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class spawnStdinThread implements Runnable{
  OutputStream outputStream;
  public spawnStdinThread(OutputStream outputStream){this.outputStream = outputStream;}
  @Override
  public void run() {
    while (true){
      try {
        Thread.sleep(2000);
        outputStream.write(System.in.readAllBytes());
        outputStream.flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
