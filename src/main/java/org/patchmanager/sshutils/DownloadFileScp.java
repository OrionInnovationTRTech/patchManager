package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.IOException;

public class DownloadFileScp {
  static Logger LOGGER = LogManager.getLogger(DownloadFileScp.class);
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and prints pwd and ls
   *
   */
  public DownloadFileScp()  {
    Session session = null;
    ChannelExec channel = null;
    try {
      session = new JSch().getSession("senas", "10.254.51.215", 22);
      session.setPassword(DotEnvUser.zitsvypassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = (ChannelExec) session.openChannel("exec");

      channel.setCommand("scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war C:\\Users\\bkarak\\Downloads");
      channel.connect();


    } catch (JSchException jse){
      LOGGER.fatal(jse.getMessage());
    } finally {
      if (channel != null) {
        channel.disconnect();
      }
      if (session != null) {
        session.disconnect();
      }
    }
  }
}


