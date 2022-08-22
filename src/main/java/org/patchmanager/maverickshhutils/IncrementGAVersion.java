package org.patchmanager.maverickshhutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static java.lang.Integer.parseInt;

public class IncrementGAVersion {
  static final Logger LOGGER = LogManager.getLogger(IncrementGAVersion.class);
  public static String incrementGAVersion(String ver, int increase){
    int verNumberPart = parseInt(ver.substring(2));
    String verCharPart = ver.substring(0,2);
    int increasedVerNumberPart = verNumberPart + increase;
    NumberFormat formatter = new DecimalFormat("00");
    if(verNumberPart + increase <= 99){
      return verCharPart + formatter.format(increasedVerNumberPart);
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
          throw new ArrayIndexOutOfBoundsException("zz99 can't go further");
        }else{
          firstChar = (char) (firstChar + 1);
        }
      }
      String result = String.valueOf(firstChar) + String.valueOf(secondChar) + overflowFixedIncVer;
      return result;
    }
  }
}
