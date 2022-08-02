package org.patchmanager.sshutils;


import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import java.util.Vector;

public class UploadFile {
  static Logger LOGGER = LogManager.getLogger(PrintPwdAndLs.class);
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and prints pwd and ls
   *
   */
  public UploadFile()  {
    Session session = null;
    Channel channel = null;
    ChannelSftp sftp = null;
    try {
      session = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session.setPassword(DotEnvUser.sshpassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = session.openChannel("sftp");
      channel.connect();

      ((ChannelSftp)channel).put("C:\\Users\\bkarak\\Documents\\dummy.txt", "/home/ntsysadm/");

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

