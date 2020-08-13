package com.github.pihme.jenkinstestbed.module1;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class FlakyTest {

  private static int counter = 0;

  @Test
  public void flakyTest() {
    System.out.println("Flaky test");
    if (counter == 0 ) {
      counter++;
      Assert.fail("failed");
    }
  }

}
