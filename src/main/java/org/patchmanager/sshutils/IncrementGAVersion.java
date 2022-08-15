package org.patchmanager.sshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.patchmanager.cli.PrintHelpCmd;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static java.lang.Integer.parseInt;

public class IncrementGAVersion {
  static final Logger LOGGER = LogManager.getLogger(IncrementGAVersion.class);
  public static void incrementGAVersion(String ver, int increase){
    if (increase > 198 || increase < 0){
      LOGGER.fatal("Invalid patch increase value");
      return;
    }
    int verNumberPart = parseInt(ver.substring(2));
    String verCharPart = ver.substring(0,2);
    int increasedVerNumberPart = verNumberPart + increase;
    NumberFormat formatter = new DecimalFormat("00");
    if(verNumberPart + increase <= 99){
      System.out.println(verCharPart + formatter.format(increasedVerNumberPart));
    }
    else {
      //if the incremented version goes past 99
      int divisionVer = increasedVerNumberPart / 100;
      String overflowFixedIncVer = formatter.format(increasedVerNumberPart % 100 + 1);
      //increase char part
      char firstChar = verCharPart.charAt(0);
      char secondChar = verCharPart.charAt(1);
      if(secondChar + divisionVer <= 'z') {
        secondChar = (char) (secondChar + divisionVer);
      }else {
        secondChar = (char) (secondChar + divisionVer - ('z' - 'a' + 1));
        if(firstChar == 'z'){
          System.out.println("zz99 can't go further");
          return;
        }else{
          firstChar = (char) (firstChar + 1);
        }
      }
      String result = String.valueOf(firstChar) + String.valueOf(secondChar) + overflowFixedIncVer;
      System.out.println(result);
    }
  }
}
