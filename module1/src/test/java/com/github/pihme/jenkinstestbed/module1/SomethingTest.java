package com.github.pihme.jenkinstestbed.module1;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class SomethingTest {

  private static final Something sut = new Something();

  @Test
  public void test() {
    // when
    boolean actual = sut.is42(42);

    //then
    assertEquals(true, actual);
  }

}
