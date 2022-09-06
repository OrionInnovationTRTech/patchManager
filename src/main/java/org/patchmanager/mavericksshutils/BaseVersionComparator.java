package org.patchmanager.mavericksshutils;

import java.util.Comparator;

public class BaseVersionComparator implements Comparator<BaseVersion> {

  @Override
  public int compare(BaseVersion o1, BaseVersion o2) {
    //if o2 is 9.8.1
    if (o1.firstDigit > o2.firstDigit){
      //10.x.x
      return 1;
    }else if(o1.firstDigit == o2.firstDigit){
      if (o1.secondDigit > o2.secondDigit){
        //9.9.x 9.10.x etc.
        return 1;
      }else if (o1.secondDigit == o2.secondDigit) {
        if (o1.thirdDigit > o2.thirdDigit){
          //9.8.2 9.8.3 etc.
          return 1;
        } else if (o1.thirdDigit == o2.thirdDigit) {
          //equal to 9.8.1
          return 0;
        }
      }
    }
    return -1;
  }
}
