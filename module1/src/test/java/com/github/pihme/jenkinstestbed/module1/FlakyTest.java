package com.github.pihme.jenkinstestbed.module1;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class FlakyTest {

  Random random = new Random();

  @Test
  public void shouldFailAlways() {
    Assert.fail("failed");
  }

  @Test
  public void shouldFailOnceInTwoInvocations() {
    if (random.nextDouble() < 0.5) {
      Assert.fail("failed");
    }
  }

  @Test
  public void shouldFailOnceInTenInvocations() {
    if (random.nextDouble() < 0.1) {
      Assert.fail("failed");
    }
  }

  @Test
  public void shouldFailOnceInHundredInvocations() {
    if (random.nextDouble() < 0.01) {
      Assert.fail("failed");
    }
  }

}
