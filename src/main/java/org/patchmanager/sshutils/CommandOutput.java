package org.patchmanager.sshutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CommandOutput {
  /**
   * Loops through the response constantly to catch when the server executes echo END_OF_COMMAND
   * returns the response as string
   * @param outputStream is the response of the server
   * @return output of the server after the command is executed
   */
  public static String getCmdOutput(ByteArrayOutputStream outputStream) throws IOException, InterruptedException {
    String output = "";
    while (outputStream.toString().indexOf("\nEND_OF_COMMAND") <= 0
     && outputStream.toString().indexOf("'s password:") <= 0
     && outputStream.toString().indexOf("Password:") <= 0) {

      //Thread.sleep(1000);
    /*ByteArrayInputStream rdr = new ByteArrayInputStream(outputStream.toByteArray());
    int read;
    byte[] buffer = new byte[1024];
    while ((read = rdr.read(buffer)) > 0){
      System.out.write(buffer,0,read);*/
    }
    //}
    output = outputStream.toString();
    outputStream.reset ();
    return "";
  }


}
