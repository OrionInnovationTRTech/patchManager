package org.patchmanager.sshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.maverickshhutils.IncrementGAVersion;
import org.patchmanager.maverickshhutils.ServerUser;

public class NewClass2 {
  static final Logger LOGGER = LogManager.getLogger(NewClass2.class);

  public void newClass2(ServerUser serverUser) {
    new IncrementGAVersion().incrementGAVersion("dm01", 1);
  }
}
