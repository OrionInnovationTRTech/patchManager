package org.patchmanager.sshutils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.maverickshhutils.ServerUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CreateSshSession {
  DotEnvUser dotEnvUserObj = new DotEnvUser();

  /**
   * Creates a client session to later create a channel and connect to a linux server
   * @param serverUser server ip usercredentials
   * @param client default client created in main
   * @return a client session to pass to another function
   * @throws IOException
   */
  public ClientSession createSshSession(ServerUser serverUser, SshClient client) throws IOException {
    client.start();
    ClientSession session = client.connect(serverUser.getUsername(), serverUser.getIp(), serverUser.getPort())
                                  .verify(2, TimeUnit.SECONDS)
                                  .getSession();
    session.addPasswordIdentity(serverUser.getPassword());
    session.auth().verify(2, TimeUnit.SECONDS);
    return session;
  }
}

