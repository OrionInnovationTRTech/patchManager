package org.patchmanager.sshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.cli.MissingOptionChecker;

import java.io.IOException;

public class NewClass {
  static final Logger LOGGER = LogManager.getLogger(BuildAndTransfer.class);

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
          channel = session.createChannel(Channel.CHANNEL_SHELL);
          try {
            channel.open().verify(2000);
            new ServerWelcomeMessage().displayWelcomeMessage(channel);
            new PassCmdToChannel().passCmdToChannel("tail -f /home/ntsysadm/test", channel);
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
