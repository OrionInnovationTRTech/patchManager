package org.patchmanager.sshutils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class ExecuteCodeLab {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public ExecuteCodeLab(String command){
    SshClient client = SshClient.setUpDefaultClient();
    client.start();

    try (ClientSession session = client.connect("ntsysadm", "47.168.150.36", 22)
        .verify(3, TimeUnit.SECONDS).getSession()) {
      session.addPasswordIdentity(DotEnvUser.labpassword);
      session.auth().verify(3, TimeUnit.SECONDS);

      try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
           ClientChannel channel = session.createChannel(Channel.CHANNEL_EXEC, command)) {
        channel.setOut(responseStream);
        try {
          channel.open().verify(3, TimeUnit.SECONDS);
          channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(7));
          String responseString = new String(responseStream.toByteArray());
          System.out.println(responseString);
        } finally {
          channel.close(false);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      client.stop();
    }
  }
}
