package org.patchmanager;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.patchmanager.mavericksshutils.CheckIfGreaterThan981.checkIfGreaterThan981;

public class CheckIfGreaterThan981Test {
  @ParameterizedTest
  @ValueSource(strings = {"9.8.2","9.9.2","9.10.1","9.10.10","10.0.0"})
  public void shouldReturnTrueForGreaterThan981(String versionBaseInput) {
    assertTrue(checkIfGreaterThan981(versionBaseInput));
  }

  @ParameterizedTest
  @ValueSource(strings = {"9.8.1","9.7.2","9.1.1","8.10.10","9.8.0"})
  public void shouldReturnFalseForGreaterThan981(String versionBaseInput) {
    assertFalse(checkIfGreaterThan981(versionBaseInput));
  }
}
