package org.patchmanager.services;

import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.common.ssh.SshException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

import static org.patchmanager.maverickshhutils.IncrementLoadNo.incrementLoadNo;
import static org.patchmanager.maverickshhutils.PrintCommandOutputLines.printCommandOutputLines;

public class CreatePatch {
  static Logger LOGGER = LogManager.getLogger(CreatePatch.class);

  /**
   * Executes patch command
   * @param shell
   * @param loadNumberOfPreviousPatch is the load number of the previous patch or the fc load if there were no previous patches
   * @param numberOfIssues is the number of jira issues gathered with RestAPI for this patch's label
   * @param fcLoadNumber is the load number of the fc number, it is the same with loadNumberOfPreviousPatch for Patch 1
   * @param versionBaseInput is the version in the form of 9.8.1
   * @param patchInput is the number of patch e.g.4
   * @param lastPom is the last label in pom.xml aa01 except when new FC is created
   * @return the ShellProcess of the ../patch/genSpidrPatch.sh -m PATCH -p patchInput -c FCFileName command
   * @throws SshException
   * @throws IOException
   */
  public static ShellProcess createPatch(ExpectShell shell, String loadNumberOfPreviousPatch,
                                          int numberOfIssues, String fcLoadNumber, String versionBaseInput,
                                         String patchInput, String lastPom) throws SshException, IOException {
    String increasedLoad = incrementLoadNo(loadNumberOfPreviousPatch, numberOfIssues);
    LOGGER.info("FC load number was " + fcLoadNumber);
    LOGGER.info("Number of issues of the previous patch is " + loadNumberOfPreviousPatch);
    LOGGER.info("Number of issues with the patch label is " + numberOfIssues);
    LOGGER.info("Load number of patch " + patchInput + " will be " + increasedLoad);
    LOGGER.info("Changing " + lastPom + " in pom.xml to: " + increasedLoad);
    printCommandOutputLines(shell.executeCommand("sed -i.bak 's/"+lastPom+"/" + increasedLoad + "/' ../pom.xml"));
    LOGGER.info("Sending mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install");
    printCommandOutputLines(shell.executeCommand("mvn -o -s ../settings.xml clean && mvn -o -s ../settings.xml install"));
    LOGGER.info("Running patch script");
    ShellProcess patchProcess;
    printCommandOutputLines(patchProcess = shell.executeCommand("../patch/genSpidrPatch.sh -m PATCH -p " + patchInput + " -c FC_" + versionBaseInput + "." + fcLoadNumber + "_Checksums.txt"));
    LOGGER.info("Patch script process ended with exit code: " + patchProcess.getExitCode());
    return patchProcess;
  }
}
