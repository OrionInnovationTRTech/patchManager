package org.patchmanager.mavericksshutils;

public class CheckIfGreaterThan981 {
  /**
   * Checks if version is greater than 9.8.1 because the patch script file changes with version
   * @param versionBaseInput
   * @return
   */
  public static boolean checkIfGreaterThan981(String versionBaseInput){
    String[] splittedVersionBaseInput = versionBaseInput.split("\\.");
    int[] digitArray = {Integer.parseInt(splittedVersionBaseInput[0]),
                                          Integer.parseInt(splittedVersionBaseInput[1]),
                                          Integer.parseInt(splittedVersionBaseInput[2])};
    if (digitArray[0] > 9){
      //10.x.x
      return true;
    }else if(digitArray[0] == 9){
      if (digitArray[1] > 8){
        //9.9.x 9.10.x etc.
        return true;
      }else if (digitArray[1] == 8) {
        if (digitArray[2] > 1){
          //9.8.2 9.8.3 etc.
          return true;
        } else if (digitArray[2] == 1) {
          //equal to 9.8.1
          return false;
        }
      }
    }
    return false;
  }
}
