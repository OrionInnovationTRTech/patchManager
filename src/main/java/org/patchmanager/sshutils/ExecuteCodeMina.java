package org.patchmanager.sshutils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class ExecuteCodeMina {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public ExecuteCodeMina(){
    String command = "ls;pwd";
    SshClient client = SshClient.setUpDefaultClient();
    client.start();

    try (ClientSession session = client.connect("senas", "10.254.51.215", 22)
        .verify(3, TimeUnit.SECONDS).getSession()) {
      session.addPasswordIdentity(DotEnvUser.zitsvypassword);
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
