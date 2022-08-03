package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MvnInstall {
  static Logger LOGGER = LogManager.getLogger(MvnInstall.class);
  static DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and sends sudo apt install maven, reads the response string
   *
   */
  public static void  sshMvnInstall()  {
    Session session = null;
    ChannelExec channel = null;
    try {
      session = new JSch().getSession("senas", "10.254.51.215", 22);
      session.setPassword(DotEnvUser.zitsvypassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = (ChannelExec) session.openChannel("exec");

      //define input stream and list to read and store the output from shell
      InputStream in = channel.getInputStream();
      List<String> result = new ArrayList<String>();


      channel.setCommand("cdwae; cd base/modules/webapps/wae-admin-rest-war/;git checkout spidr_4.8.1_patch && git pull; mvn -s ../settings.xlm clean && mvn -s ../settings.xml install");

      // if true ==>  force the pseudo terminal allocation for the "exec" channel
      ((ChannelExec) channel).setPty(true);
      channel.setErrStream(System.err);
      channel.connect();

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;

      while ((line = reader.readLine()) != null)
      {
        System.out.println(line);
        result.add(line);
      }
      int exitStatus = channel.getExitStatus();
      System.out.println("Exit status code: "+exitStatus);
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