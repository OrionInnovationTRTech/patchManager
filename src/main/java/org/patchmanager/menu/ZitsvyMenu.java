package org.patchmanager.menu;

import com.sshtools.common.ssh.SshException;

import java.io.IOException;

import static org.patchmanager.menu.DisplayMenu.displayMenu;
import static org.patchmanager.menu.MainMenu.*;
import static org.patchmanager.services.FileTransfer.fileTransfer;
import static org.patchmanager.services.GitWaeAllMavenBuild.gitwaeallMavenBuild;
import static org.patchmanager.services.MavenBuildAndPatchCreationSeq.mavenBuildAndPatchCreationSeq;
import static org.patchmanager.services.PatchCreation.patchCreation;
import static org.patchmanager.services.PseudoTerminal.pseudoTerminal;
import static org.patchmanager.services.ServerCredentials.serverUserCredentials;
import static org.patchmanager.services.WaeAdminResWarMavenBuild.waeAdminResWarMavenBuild;

public class ZitsvyMenu {
  public static void zitsvyMenu() throws IOException, SshException {
    String choice = "0";
    zitsvyLoop : while(true) {
      displayMenu("Zitsvy Services Menu", zitsvyMenuItems);

      System.out.print("Enter Your Choice: ");
      choice = scanner.nextLine();

      switch (choice) {
        case "1"://Enter lab credentials
          zitsvyUsr = serverUserCredentials();
          break;
        case "2"://Pseudoterminal
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            pseudoTerminal(zitsvyUsr);
          }
          break;
        case "3"://File transfer
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            fileTransfer(zitsvyUsr);
          }
          break;
        case "4"://Maven build in cdwae
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            waeAdminResWarMavenBuild(zitsvyUsr);
          }
          break;
        case "5"://Maven Build in gitwaeall
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            gitwaeallMavenBuild(zitsvyUsr);
          }
          break;
        case "6"://Patch Creation
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            patchCreation(zitsvyUsr);
          }
          break;
        case "7"://Maven Build and Patch Creation sequentially
          if (zitsvyUsr == null) {
            System.out.println("Enter your credentials with the first choice first");
            break;
          } else {
            mavenBuildAndPatchCreationSeq(zitsvyUsr);
          }
          break;
        case "8"://Return to main menu
          break zitsvyLoop;
        case "9":
          System.out.println("Terminating the program");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid menu choice; try again.");
          break;
      }
    }
  }
}