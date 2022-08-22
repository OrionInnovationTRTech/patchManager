package org.patchmanager.maverickshhutils;

import com.sshtools.client.*;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellProcessController;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.common.ssh.SshException;
import org.patchmanager.apiutils.DotEnvUser;
import org.patchmanager.sshutils.ServerUser;

import java.io.IOException;

import static org.patchmanager.maverickshhutils.IncrementGAVersion.LOGGER;
import static org.patchmanager.maverickshhutils.IncrementGAVersion.incrementGAVersion;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class MaverickFCCreation {
  DotEnvUser dotEnvUserObj = new DotEnvUser();
  public void maverickFCCreation(ServerUser serverUser, String versionInput, String fixCount) throws IOException, SshException {
    //get the version
    //get the last part of version
    //get increase
    String[] splitVersionByDot = versionInput.split("\\.");
    String gaVersion = splitVersionByDot[3];
    String versionHigher = String.join(".", splitVersionByDot[0], splitVersionByDot[1], splitVersionByDot[2]);
    String increasedGA = incrementGAVersion(gaVersion, Integer.parseInt(fixCount));
    try(SshClient ssh = new SshClient(serverUser.getIp(), serverUser.getPort(), serverUser.getUsername(), serverUser.getPassword().toCharArray())) {
      ssh.runTask(new ShellTask(ssh) {
        @Override protected void onOpenSession(SessionChannelNG session) throws IOException, SshException, ShellTimeoutException {

          ExpectShell shell = new ExpectShell(this);
          printCommandOutputLines(shell.executeCommand("gitwaeall"));
          printCommandOutputLines(shell.executeCommand("git checkout spidr_4.8.1_patch"));
          printCommandOutputLines(shell.executeCommand("git pull"));
          printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ increasedGA +"/' ../pom.xml"));
          LOGGER.info("-p "+ fixCount +" -c FC_"+ versionHigher+"."+ increasedGA +"_Checksums.txt");
          printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
          //printCommandOutputLines(shell.executeCommand("../patch/genSpidrPatch.sh -m FC"));
          printCommandOutputLines(shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p "+ fixCount +" -c FC_"+ versionHigher+"."+ increasedGA +"_Checksums.txt"));
        }
      });
      //take branch as input
      //gai jiradan al
      //patch 1 degilse
      //GA 4.8.1.dm10
      //passwordu alma
    };
  }

}
