package org.patchmanager.sshutils;

import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ServerWelcomeMessage {
  /**
   * Waits and returns the initial output the server returns when a connection is established
   * @param channel
   * @throws Exception
   */
  public void displayWelcomeMessage(ClientChannel channel) throws Exception {
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    channel.setOut(responseStream);
    channel.setErr(errorStream);
    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 500);
    System.out.println(new String(responseStream.toByteArray()));
    String error = new String(errorStream.toByteArray());
    if (!error.isEmpty()) {
      throw new Exception(error);
    }
  }
}

