package org.patchmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.patchmanager.io.WriteOutro.writeOutro;

public class WriteOutroTest {
  @Test
  public void writeOutroTest() {
    String expected = "\n" +
        "END DETAILED_DESCRIPTION\n" +
        "KANDYLINK_" + "9.8.1" + "_P_" + "4" + ".tar.gz\n\n";
    assertEquals(expected, writeOutro("4", "9.8.1"));
  }
}
