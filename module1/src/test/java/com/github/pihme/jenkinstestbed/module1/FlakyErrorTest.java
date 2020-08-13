package com.github.pihme.jenkinstestbed.module1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FlakyErrorTest {

  private static int counter = 0;

  @Before
  public void setUp() {
    System.out.println("Flaky error");
    if (counter == 0 ) {
      counter++;
      throw new RuntimeException("Oops, something happeeed");
    }
  }

  @Test
  public void failNever() {
  }
}
