package org.patchmanager.sshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;

public class BuildAndTransfer {
  static final Logger LOGGER = LogManager.getLogger(BuildAndTransfer.class);

  /**
   * In /wae-admin-rest-war
   * After git checkout and pull
   * Cleans and builds maven and then sends the war file to lab's /tmp folder using scp
   * @param serverUser
   */
  public void buildAndTransfer(ServerUser serverUser){
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
            new PassCmdToChannel().passCmdToChannel("cdwae", channel);
            new PassCmdToChannel().passCmdToChannel("cd base/modules/webapps/wae-admin-rest-war/", channel);
            new PassCmdToChannel().passCmdToChannel("git checkout spidr_4.8.1_patch", channel);
            new PassCmdToChannel().passCmdToChannel("git pull", channel);
            new PassCmdToChannel().passCmdToChannel("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install", channel);
            new PassCmdToChannel().passCmdToChannel("scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war ntsysadm@47.168.150.36:/tmp/", channel);
            new PassCmdToChannel().passPasswordToChannel(DotEnvUser.zitsvypassword, channel);
            new PassCmdToChannel().passPasswordToChannel(DotEnvUser.labpassword, channel);
            new PassCmdToChannel().passCmdToChannel("scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/loadbuild/patch/Patch_2_Checksums_9.8.1.dm95.txt  ntsysadm@47.168.150.36:/tmp/", channel);
            new PassCmdToChannel().passPasswordToChannel(DotEnvUser.zitsvypassword, channel);
            new PassCmdToChannel().passPasswordToChannel(DotEnvUser.labpassword, channel);
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
