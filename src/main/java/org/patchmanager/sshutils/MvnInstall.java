package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;


public class MvnInstall {
  static Logger LOGGER = LogManager.getLogger(MvnInstall.class);

  /**
   * Connects to ssh server and sends sudo apt install maven, reads the response string
   *
   */
  public static void  SshMvnInstall()  {
    Session session = null;
    ChannelExec channel = null;
    try {
      session = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session.setPassword("RAPtor1234");
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = (ChannelExec) session.openChannel("exec");
      channel.setCommand("sudo apt install maven");
      ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
      channel.setOutputStream(responseStream);
      channel.connect();

      while (channel.isConnected()) {
        Thread.sleep(50);
      }

      String responseString = new String(responseStream.toByteArray());
      System.out.println(responseString);
    } catch (JSchException jse){
      LOGGER.fatal(jse.getMessage());
    } catch (InterruptedException ie){
      LOGGER.fatal(ie.getMessage());
    }
    finally {
      if (channel != null) {
        channel.disconnect();
      }
      if (session != null) {
        session.disconnect();
      }

    }
  }
}
