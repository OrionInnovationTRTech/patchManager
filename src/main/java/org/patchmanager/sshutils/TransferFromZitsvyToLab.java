package org.patchmanager.sshutils;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.apiutils.DotEnvUser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TransferFromZitsvyToLab {
  static Logger LOGGER = LogManager.getLogger(TransferFromZitsvyToLab.class);
  static DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Connects to ssh server and sends sudo apt install maven, reads the response string
   *
   */
  public static void  transferFromZitsvyToLab()  {
    Session session = null;
    Session session2 = null;
    ChannelExec channel = null;
    ChannelExec channel2 = null;
    try {
      session = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session.setPassword(DotEnvUser.labpassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      session2 = new JSch().getSession("senas", "10.254.51.215", 22);
      session2.setPassword(DotEnvUser.zitsvypassword);
      session2.setConfig("StrictHostKeyChecking", "no");
      session2.connect();



      channel = (ChannelExec) session.openChannel("exec");
      channel2 = (ChannelExec) session2.openChannel("exec");
      //define input stream and list to read and store the output from shell
      InputStream in = channel.getInputStream();
      InputStream in2 = channel2.getInputStream();
      OutputStream os = channel.getOutputStream();
      OutputStream os2 = channel2.getOutputStream();
      String command = "scp senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war ntsysadm@47.168.150.36:/tmp/";
      channel.setCommand(command);
      // if true ==>  force the pseudo terminal allocation for the "exec" channel
      channel.setPty(true);
      channel.setErrStream(System.err);
      channel2.setPty(true);
      channel2.setErrStream(System.err);
      channel.connect();
      channel2.connect();


      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      System.out.println(reader.readLine());
      /*while ((line = reader.readLine()) != null)
      {
        System.out.println(line);
      }*/
      BufferedReader reader2 = new BufferedReader(new InputStreamReader(in2));
      String line2;

      while ((line2 = reader2.readLine()) != null)
      {
        System.out.println(line2);
      }

      int exitStatus = channel.getExitStatus();
      System.out.println("Exit status code: "+exitStatus);
      int exitStatus2 = channel2.getExitStatus();
      System.out.println("Exit status code: "+exitStatus2);
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
      if (session2 != null) {
        session.disconnect();
      }
    }
  }
}

