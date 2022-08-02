package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.io.WriteOutro;

import java.io.*;
import java.util.Vector;

public class Ssh {
  static Logger LOGGER = LogManager.getLogger(Ssh.class);
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and sends a command, reads the response string
   * @param command is for the ssh command
   */
  public static void  SshExecuteCommand(String command)  {
    Session session = null;
    Channel channel = null;

    try {
      session = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session.setPassword(DotEnvUser.sshpassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = (ChannelExec) session.openChannel("exec");
      InputStream is = channel.getInputStream();
      ((ChannelExec) channel).setCommand(command);
      //Print error
      ((ChannelExec) channel).setErrStream(System.err);
      channel.connect();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
      String line;
      while((line = bufferedReader.readLine()) != null){
        System.out.println(line);
      }

      /*while (channel.isConnected()) {
        Thread.sleep(50);
      }*/
    } catch (JSchException jse){
      LOGGER.fatal(jse.getMessage());
    } catch (IOException ioe){
      LOGGER.fatal(ioe.getMessage());
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
