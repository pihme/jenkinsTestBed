package com.github.pihme.jenkinstestbed.module1;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class FlakyTest {

  Random random = new Random();


  @Test
  public void shouldFailNineInTenInvocations() {
    System.out.println(System.currentTimeMillis());
    if (random.nextDouble() < 0.9) {
      Assert.fail("failed");
    }
  }

  @Test
  public void shouldFailOnceInTwoInvocations() {
    System.out.println(System.currentTimeMillis());
    if (random.nextDouble() < 0.5) {
      Assert.fail("failed");
    }
  }

  @Test
  public void shouldFailOnceInTenInvocations() {
    System.out.println(System.currentTimeMillis());
    if (random.nextDouble() < 0.1) {
      Assert.fail("failed");
    }
  }

  @Test
  public void shouldFailOnceInHundredInvocations() {
    System.out.println(System.currentTimeMillis());
    if (random.nextDouble() < 0.01) {
      Assert.fail("failed");
    }
  }

}
