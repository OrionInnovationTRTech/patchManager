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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PipedShell {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public PipedShell(ServerUser serverUser) throws Exception {
    SshClient client = SshClient.setUpDefaultClient();
    client.start();
    try (ClientSession session = client.connect(serverUser.getUsername(), serverUser.getIp(), serverUser.getPort())
        .verify(1, TimeUnit.SECONDS)
        .getSession()) {
      session.addPasswordIdentity(serverUser.getPassword());
      session.auth()
          .verify(1, TimeUnit.SECONDS);
      try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
           ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
           ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)){
        channel.setOut(responseStream);
        channel.setErr(errorStream);
        try {
          channel.open().verify(3, TimeUnit.SECONDS);
          OutputStream pipedIn = channel.getInvertedIn();

          Scanner scanner = new Scanner(System.in);
          String cmd = "";
          channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 500);
          System.out.println(new String(responseStream.toByteArray()));
          while (true) {
            System.out.println("Write a linux or command or write !! to exit");
            cmd = scanner.nextLine() + "\n";
            if (cmd.equals("!!\n")){
              break;
            }
            pipedIn.write(cmd.getBytes());
            pipedIn.flush();
            channel.waitFor(EnumSet.of(ClientChannelEvent.EOF), 125);
            System.out.println(new String(responseStream.toByteArray()));
            responseStream.reset();
          }
          System.out.println("Terminating the program");
          String error = new String(errorStream.toByteArray());
          if(!error.isEmpty()) {
            throw new Exception(error);
          }
        } finally {
          channel.close(false);
        }
      }
    } finally {
      client.stop();
    }
  }
}

