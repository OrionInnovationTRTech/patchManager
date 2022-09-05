package org.patchmanager.services;

import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.mavericksshutils.PrintCommandOutputLines.printCommandOutputLines;

public class CreateFC {
  static Logger LOGGER = LogManager.getLogger(CreateFC.class);
  public static ShellProcess createFC(ExpectShell shell,String loadNumberOfFcForCreation,Scanner scanner) throws SshException, IOException {
    LOGGER.info("Changing aa01 in pom.xml to: " + loadNumberOfFcForCreation);
    printCommandOutputLines(shell.executeCommand("sed -i.bak 's/aa01/"+ loadNumberOfFcForCreation +"/' ../pom.xml"));
    LOGGER.info("Sending mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install");
    printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
    LOGGER.info("Running FC script");
    ShellProcess fcProcess;
    printCommandOutputLines(fcProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m FC"));
    LOGGER.info("FC script process ended with exit code: " + fcProcess.getExitCode());
    return fcProcess;
  }
}
