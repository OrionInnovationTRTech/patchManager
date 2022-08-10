package org.patchmanager.sshutils;

import org.apache.sshd.client.channel.ClientChannel;
import java.io.*;

public class PassCmdToChannel {
  public void passCmdToChannel(String command, ClientChannel channel ) throws Exception {
    command = command + ";echo \"END_OF_COMMAND\"\n";
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    OutputStream pipedIn = channel.getInvertedIn();
    channel.setOut(responseStream);
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    channel.setErr(errorStream);
    pipedIn.write(command.getBytes());
    pipedIn.flush();
    System.out.println(getCmdOutput(responseStream));
    String error = new String(errorStream.toByteArray());
    if (!error.isEmpty()) {
      throw new Exception(error);
    }
  }
  public String getCmdOutput(ByteArrayOutputStream outputStream) {
    while (outputStream.toString().indexOf("\nEND_OF_COMMAND") <= 0 && outputStream.toString().indexOf("'s password:") <= 0) {}
    String output = outputStream.toString ();
    outputStream.reset ();
    return output;
  }
  public void passPasswordToChannel(String command, ClientChannel channel ) throws Exception {
    command = command + "\n";
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    OutputStream pipedIn = channel.getInvertedIn();
    channel.setOut(responseStream);
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    channel.setErr(errorStream);
    pipedIn.write(command.getBytes());
    pipedIn.flush();
    System.out.println(getCmdOutput(responseStream));
    String error = new String(errorStream.toByteArray());
    if (!error.isEmpty()) {
      throw new Exception(error);
    }
  }

}

