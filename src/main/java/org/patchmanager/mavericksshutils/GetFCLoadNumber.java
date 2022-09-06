package org.patchmanager.mavericksshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetFCLoadNumber {
  private GetFCLoadNumber(){
    throw new IllegalStateException("Utility class");
  }
  static Logger LOGGER = LogManager.getLogger(GetFCLoadNumber.class);
  /**
   * Gets the load number part of the FC file
   * For FC_9.8.1.dm64_Checksums.txt it is dm64
   * @param fc
   * @return
   */
  public static String getFcLoadNumber(String fc) {
    //FC_9.8.1.dm64_Checksums.txt to FC,9.8.1.dm64,Checksums.txt
    String[] fcSplitted = fc.split("_");
    LOGGER.info("The fc version is {}", fcSplitted[1]);
    //9.8.1.dm64 to just get the last 4 things
    return fcSplitted[1].substring(fcSplitted[1].length() - 4);
  }
}
