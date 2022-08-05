package org.patchmanager.sshutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.scp.client.ScpRemote2RemoteTransferHelper;
import org.apache.sshd.scp.common.ScpTransferEventListener;
import org.apache.sshd.scp.server.ScpCommandFactory;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.threadsForSsh.spawnStderrThread;
import org.patchmanager.threadsForSsh.spawnStdinThread;
import org.patchmanager.threadsForSsh.spawnStdoutThread;

public class MinaTest {
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  public MinaTest() throws InterruptedException {
    SshClient clientZitsvy = SshClient.setUpDefaultClient();
    clientZitsvy.start();

    Thread serrt = null;
    Thread sint = null;
    Thread soutt = null;
    try {
      ClientSession sessionZitsvy = clientZitsvy.connect("senas", "10.254.51.215", 22)
          .verify(3, TimeUnit.SECONDS).getSession();
      sessionZitsvy.addPasswordIdentity(DotEnvUser.zitsvypassword);
      sessionZitsvy.auth().verify(3, TimeUnit.SECONDS);

      ClientChannel channel = sessionZitsvy.createShellChannel();
      ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
      ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
      channel.setOut(responseStream);
      channel.setErr(errorStream);

      channel.open().verify(2, TimeUnit.SECONDS);
      spawnStdinThread ssin = new spawnStdinThread(channel.getInvertedIn());
      spawnStdoutThread ssout = new spawnStdoutThread(channel.getInvertedOut());
      spawnStderrThread sserr = new spawnStderrThread(channel.getInvertedErr());
      sint = new Thread(ssin);
      soutt = new Thread(ssout);
      serrt = new Thread(sserr);
      sint.start();
      soutt.start();
      serrt.start();
      channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0L);

    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      sint.join();
      soutt.join();
      serrt.join();


    }

  }
}
