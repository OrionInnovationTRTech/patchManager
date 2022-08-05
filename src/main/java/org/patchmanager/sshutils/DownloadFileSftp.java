package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

public class DownloadFileSftp {
  static Logger LOGGER = LogManager.getLogger(DownloadFileSftp.class);
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and prints pwd and ls
   *
   */
  public DownloadFileSftp()  {
    Session session = null;
    Channel channel = null;
    ChannelSftp sftp = null;
    try {
      session = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session.setPassword(DotEnvUser.labpassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      /*session = new JSch().getSession("senas", "10.254.51.215", 22);
      session.setPassword(DotEnvUser.zitsvypassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();*/

      channel = session.openChannel("sftp");
      channel.connect();
      sftp= (ChannelSftp) channel;
      //sftp.put("/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war", "ntsysadm@47.168.150.36:/tmp/");
      sftp.get("senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war", "/tmp/");
    } catch (JSchException jse){
      LOGGER.fatal(jse.getMessage());
    } catch (SftpException sftpe){
      LOGGER.fatal(sftpe.getMessage());
    }
    finally {
      // Validating if channel sftp is not null to exit
      if (sftp != null) {
        sftp.exit();
        System.out.println("Sftp exited");
      }
      if (channel != null) {
        channel.disconnect();
      }
      if (session != null) {
        session.disconnect();
      }


    }
  }
}

