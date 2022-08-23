package org.patchmanager.sshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.future.WaitableFuture;
import org.apache.sshd.common.util.io.input.NoCloseInputStream;
import org.apache.sshd.common.util.io.output.NoCloseOutputStream;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Set;

public class NewClass {
  static final Logger LOGGER = LogManager.getLogger(NewClass.class);

  /**
   * In /wae-admin-rest-war
   * After git checkout and pull
   * Cleans and builds maven and then sends the war file to lab's /tmp folder using scp
   * @param serverUser
   */
  public void newClass(ServerUser serverUser){
    SshClient client = SshClient.setUpDefaultClient();
    try {
      ClientSession session = null;
      try {
        session = new CreateSshSession().createSshSession(serverUser, client);
        ClientChannel channel = null;
        try {
          String command = "su";
          channel = session.createExecChannel("su");
          //channel = session.createShellChannel();
          try {
            //channel.setStreaming(ClientChannel.Streaming.Async);
            new ServerWelcomeMessage().displayWelcomeMessage(channel);
            NoCloseInputStream ncis = new NoCloseInputStream(System.in);
            NoCloseOutputStream ncos = new NoCloseOutputStream(System.out);
            NoCloseOutputStream ncee = new NoCloseOutputStream(System.err);
            channel.setIn(ncis);
            channel.setOut(ncos);
            channel.setErr(ncee);
            WaitableFuture wf = channel.open();
            Set<ClientChannelEvent> waitMask = channel.waitFor(List.of(ClientChannelEvent.CLOSED), 0L);
            if (waitMask.contains(ClientChannelEvent.TIMEOUT)) {
              throw new SocketTimeoutException("Failed to retrieve command result in time: " + command);
            }
            Integer exitStatus = channel.getExitStatus();
            System.out.println("Exit status :" + exitStatus);


          } catch (Exception e) {
            LOGGER.fatal("Problem opening channel and sending commands" + e.getMessage());
          }
        } catch (Exception e) {
          LOGGER.fatal("Problem creating channel "+ e.getMessage());
        } finally {
          try {
            channel.close();
          }catch (IOException ioe){
            LOGGER.fatal("Problem closing channel "+ ioe.getMessage());
          }
        }
      }catch (Exception e){
        LOGGER.fatal("Problem creating session "+ e.getMessage());
      }finally {
        try {
          session.close();
        }catch (IOException ioe){
          LOGGER.fatal("Problem closing session "+ ioe.getMessage());
        }
      }
    } catch (Exception e) {
      LOGGER.fatal("Problem creating client "+ e.getMessage());
    } finally {
      try {
        client.close();
      }catch (IOException ioe){
        LOGGER.fatal("Problem closing client "+ ioe.getMessage());
      }
    }
  }
}
