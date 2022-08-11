package org.patchmanager.sshutils;

import org.apache.sshd.client.channel.ClientChannel;
import java.io.*;

public class PassCmdToChannel {
  /**
   * Writes the command to channel and prints out the output, adds ;echo END_OF_COMMAND to the end of the command
   * to immediately return when server is done
   * @param command to be passed
   * @param channel designated channel
   * @throws Exception When there is an error uses error stream and throws exception
   */
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

  /**
   * Loops through the response constantly to catch when the server executes echo END_OF_COMMAND
   * returns the respons as string
   * @param outputStream is the response of the server
   * @return output of the server after the command is executed
   */
  public String getCmdOutput(ByteArrayOutputStream outputStream) {
    while (outputStream.toString().indexOf("\nEND_OF_COMMAND") <= 0 && outputStream.toString().indexOf("'s password:") <= 0) {}
    String output = outputStream.toString ();
    outputStream.reset ();
    return output;
  }

  /**
   * Used to pass the passwords to the channel
   * Since the normal command passing function adds echo END_OF_COMMAND to the end,
   * and the passwords are needed to be unaltered, this function uses regular timeout
   * instead of getCmdOutput function to wait for response
   * @param password password
   * @param channel is the channel
   * @throws Exception uses error stream
   */
  public void passPasswordToChannel(String password, ClientChannel channel ) throws Exception {
    password = password + "\n";
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    OutputStream pipedIn = channel.getInvertedIn();
    channel.setOut(responseStream);
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    channel.setErr(errorStream);
    pipedIn.write(password.getBytes());
    pipedIn.flush();
    System.out.println(getCmdOutput(responseStream));
    String error = new String(errorStream.toByteArray());
    if (!error.isEmpty()) {
      throw new Exception(error);
    }
  }

}

