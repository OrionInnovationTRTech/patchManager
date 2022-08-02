package org.patchmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.patchmanager.io.WriteIntro.writeIntro;

public class WriteIntroTest {
  @Test
  public void writeIntroTest() {
    String expected = "PRODUCT_LINE: KANDYLINK\n" +
        "============================================\n" +
        "\n" +
        "CATEGORY: GEN\n" +
        "PREREQUISITES: \n" +
        "END\n" +
        "PATCH ID: KANDYLINK_9.8.1.dl35_P_4\n" +
        "LOADS: KANDYLINK_9.8.1\n" +
        "END\n" +
        "STATUS: V\n" +
        "WEB_POST: Y\n" +
        "STATUS DATE: 20220719\n" +
        "TITLE: Patch 4\n" +
        "DETAILED_DESCRIPTION:\n" +
        "This patch includes all fix that were previously released in earlier patches and therefore only the latest patches needs to be applied. \n" +
        "For a complete list of fixes in this patch please refer to the individual patch admin files from the previously released patches.\n" +
        "Please check to KANDYLINK 4.8.1 Patch 4 Release Notes for details.\n" +
        "\n" +
        "Includes fixes for following issues:\n";
    assertEquals(expected, writeIntro("4", "4.8.1", "9.8.1", "9.8.1.dl35", "20220719"));
  }
}
