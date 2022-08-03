package org.patchmanager.sshutils;


import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CheckFileExists {
  static Logger LOGGER = LogManager.getLogger(CheckFileExists.class);
  static DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and prints pwd and ls
   *
   */
  public static boolean checkFileExists()  {
    Session session = null;
    Channel channel = null;
    try {
      session = new JSch().getSession("senas", "10.254.51.215", 22);
      session.setPassword(DotEnvUser.zitsvypassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      channel = (ChannelExec) session.openChannel("exec");

      //define input stream and list to read and store the output from shell
      InputStream in = channel.getInputStream();
      List<String> result = new ArrayList<String>();


      ((ChannelExec) channel).setCommand("cd /export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target; ls");

      // if true ==>  force the pseudo terminal allocation for the "exec" channel
      ((ChannelExec) channel).setPty(false);
      ((ChannelExec) channel).setErrStream(System.err);
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
      for(int i = 0; i < result.size(); i++) {

        if(result.get(i).equals("wae-admin-rest-war-9.8.1.war")) {
          System.out.println("File is present");
          channel.disconnect();
          session.disconnect();
          return true;
        }

        if(i == result.size()-1) {
          System.out.println("File is not present");
          channel.disconnect();
          session.disconnect();
          return false;
        }
      }

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
    return false;
  }
}


