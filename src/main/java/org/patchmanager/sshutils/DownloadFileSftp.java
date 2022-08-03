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

      sftp = (ChannelSftp) channel;

      sftp.put("/home/ntsysadm/dummy.txt", "/bkarak/Downloads");

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

