package com.github.pihme.jenkinstestbed.module2;


import org.junit.Assert;
import org.junit.Test;

public class FailingTest {

    @Test
    public void failAlways() {
        Assert.fail();
    }
}
