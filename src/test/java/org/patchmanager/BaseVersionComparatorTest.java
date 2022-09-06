package org.patchmanager;

import org.junit.jupiter.api.Test;
import org.patchmanager.mavericksshutils.BaseVersion;
import org.patchmanager.mavericksshutils.BaseVersionComparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseVersionComparatorTest {
  @Test
  public void should1981Greater() {
    BaseVersion o1 = new BaseVersion("19.8.1");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(1, bvc.compare(o1,o2));
  }
  @Test
  public void should991Greater() {
    BaseVersion o1 = new BaseVersion("9.9.1");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(1, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn982Greater() {
    BaseVersion o1 = new BaseVersion("9.8.2");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(1, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn981Equal() {
    BaseVersion o1 = new BaseVersion("9.8.1");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(0, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn980Lesser() {
    BaseVersion o1 = new BaseVersion("9.8.0");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(-1, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn971Lesser() {
    BaseVersion o1 = new BaseVersion("9.7.1");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(-1, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn881Lesser() {
    BaseVersion o1 = new BaseVersion("8.8.1");
    BaseVersion o2 = new BaseVersion("9.8.1");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(-1, bvc.compare(o1,o2));
  }
  @Test
  public void shouldReturn990and991Greater() {
    BaseVersion o1 = new BaseVersion("9.9.1");
    BaseVersion o2 = new BaseVersion("9.9.0");
    BaseVersionComparator bvc = new BaseVersionComparator();
    assertEquals(1, bvc.compare(o1,o2));
  }

}
