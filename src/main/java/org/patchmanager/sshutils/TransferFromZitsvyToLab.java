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
    try {
      session2 = new JSch().getSession("ntsysadm", "47.168.150.33", 22);
      session2.setPassword(DotEnvUser.labpassword);
      session2.setConfig("StrictHostKeyChecking", "no");
      session2.connect();

      session = new JSch().getSession("senas", "10.254.51.215", 22);
      session.setPassword(DotEnvUser.zitsvypassword);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();



      channel = (ChannelExec) session.openChannel("exec");

      //define input stream and list to read and store the output from shell
      InputStream in = channel.getInputStream();
      List<String> result = new ArrayList<String>();
      String command = "scp -3 senas@10.254.51.215:/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war C:\\Users\\bkarak\\Downloads";
      channel.setCommand(command);

      // if true ==>  force the pseudo terminal allocation for the "exec" channel
      channel.setPty(true);
      channel.setErrStream(System.err);
      channel.connect();

      SshBase 

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
      if (session2 != null) {
        session.disconnect();
      }
    }
  }
}

