package org.patchmanager.mavericksshutils;

public class BaseVersion {
  String baseVer;
  int firstDigit;
  int secondDigit;
  int thirdDigit;

  public BaseVersion(String versionBaseInput){
    String[] splittedVersionBaseInput = versionBaseInput.split("\\.");
    this.baseVer = versionBaseInput;
    this.firstDigit = Integer.parseInt(splittedVersionBaseInput[0]);
    this.secondDigit = Integer.parseInt(splittedVersionBaseInput[1]);
    this.thirdDigit = Integer.parseInt(splittedVersionBaseInput[2]);
  }
}
