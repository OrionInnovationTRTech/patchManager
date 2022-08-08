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

public class PassCmdToChannel {
  public void passCmdToChannel(String command, ClientChannel channel) throws Exception {
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    OutputStream pipedIn = channel.getInvertedIn();
    channel.setOut(responseStream);
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

    channel.setErr(errorStream);

    pipedIn.write(command.getBytes());
    pipedIn.flush();
    channel.waitFor(EnumSet.of(ClientChannelEvent.EOF), TimeUnit.SECONDS.toMillis(3));
    System.out.println(new String(responseStream.toByteArray()));
    String error = new String(errorStream.toByteArray());
    if (!error.isEmpty()) {
      throw new Exception(error);
    }
  }
}

