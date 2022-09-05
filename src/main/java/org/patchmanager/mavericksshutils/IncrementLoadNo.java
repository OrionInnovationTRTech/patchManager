package org.patchmanager.mavericksshutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static java.lang.Integer.parseInt;

public class IncrementLoadNo {
  static final Logger LOGGER = LogManager.getLogger(IncrementLoadNo.class);

  /**
   * Increases load number by increase amount, variable loadNoNumberPart is for 64 in dm64
   * and variable verCharPart is is for dm in dm64
   * @param loadNo
   * @param increase
   * @return
   */
  public static String incrementLoadNo(String loadNo, int increase){
    LOGGER.info("Starting load is: "+loadNo);
    int loadNoNumberPart = parseInt(loadNo.substring(2));
    String loadNoCharPart = loadNo.substring(0,2);
    int increasedLoadNoNumberPart = loadNoNumberPart + increase;
    NumberFormat formatter = new DecimalFormat("00");
    if(loadNoNumberPart + increase <= 99){
      String newLoad = loadNoCharPart + formatter.format(increasedLoadNoNumberPart);
      LOGGER.info("New load with an increase of "+ increase +" is: " + newLoad);
      return newLoad;
    }
    else {
      //if the incremented loadNo goes past 99
      int divisionVer = increasedLoadNoNumberPart / 100;
      String overflowFixedIncVer = formatter.format(increasedLoadNoNumberPart % 100 + 1);
      //increase char part
      char firstChar = loadNoCharPart.charAt(0);
      char secondChar = loadNoCharPart.charAt(1);
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
      String newLoad = String.valueOf(firstChar) + String.valueOf(secondChar) + overflowFixedIncVer;
      LOGGER.info("New load with an increase of "+ increase +" is: " + newLoad);
      return newLoad;
    }
  }
}
