package org.patchmanager.sshutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.scp.client.ScpRemote2RemoteTransferHelper;
import org.apache.sshd.scp.common.ScpTransferEventListener;
import org.patchmanager.apiutils.DotEnvUser;

public class ScpMina {
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  public ScpMina(){
    SshClient clientZitsvy = SshClient.setUpDefaultClient();
    SshClient clientLab = SshClient.setUpDefaultClient();
    clientZitsvy.start();
    clientLab.start();

    try{
      ClientSession sessionLab = clientLab.connect("ntsysadm", "47.168.150.33", 22)
          .verify(3, TimeUnit.SECONDS).getSession();
      sessionLab.addPasswordIdentity(DotEnvUser.labpassword);
      sessionLab.auth().verify(3, TimeUnit.SECONDS);

      ClientSession sessionZitsvy = clientZitsvy.connect("senas", "10.254.51.215", 22)
          .verify(3, TimeUnit.SECONDS).getSession();
      sessionZitsvy.addPasswordIdentity(DotEnvUser.zitsvypassword);
      sessionZitsvy.auth().verify(3, TimeUnit.SECONDS);



      ScpRemote2RemoteTransferHelper helper = new ScpRemote2RemoteTransferHelper(sessionZitsvy, sessionLab);
      System.out.println(helper.getDestinationSession());
      helper.transferFile("/export/viewstore/disk24/mcs/wam/gitstorage/senas/Kandy_Link/wae/base/modules/webapps/wae-admin-rest-war/target/wae-admin-rest-war-9.8.1.war"
          ,"/tmp/", true);




    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
