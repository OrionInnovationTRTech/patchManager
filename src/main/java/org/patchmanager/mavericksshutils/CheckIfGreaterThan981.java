package org.patchmanager.mavericksshutils;

public class CheckIfGreaterThan981 {
  /**
   * Checks if version is greater than 9.8.1 because the patch script file changes with version
   * @param versionBaseInput
   * @return
   */
  public static boolean checkIfGreaterThan981(String versionBaseInput){
    BaseVersion bv = new BaseVersion(versionBaseInput);
    BaseVersion nineeightone = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    return bvc.compare(bv, nineeightone) > 0;
  }
}
