package org.patchmanager.sshutils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class ShellCodeLab {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public ShellCodeLab(String command) throws Exception {
    command = command + "\n";
    SshClient client = SshClient.setUpDefaultClient();
    client.start();
    try (ClientSession session = client.connect("ntsysadm", "47.168.150.36", 22)
        .verify(1, TimeUnit.SECONDS)
        .getSession()) {
      session.addPasswordIdentity(DotEnvUser.labpassword);
      session.auth()
          .verify(1, TimeUnit.SECONDS);
      try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
           ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
           ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)){
        channel.setOut(responseStream);
        channel.setErr(errorStream);
        try {
          //channel.isOpen();
          channel.open().verify(1, TimeUnit.SECONDS);
          OutputStream pipedIn = channel.getInvertedIn();
          pipedIn.write(command.getBytes());
          pipedIn.flush();
          channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(3));
          String error = new String(errorStream.toByteArray());
          if(!error.isEmpty()) {
            throw new Exception(error);
          }
          System.out.println(new String(responseStream.toByteArray()));
        } finally {
          channel.close(false);
        }
      }
    } finally {
      client.stop();
    }
  }
}

