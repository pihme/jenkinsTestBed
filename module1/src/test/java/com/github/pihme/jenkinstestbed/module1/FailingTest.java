package com.github.pihme.jenkinstestbed.module1;

import org.junit.Assert;
import org.junit.Test;

public class FailingTest {

    @Test
    public void failAlways() {
        System.out.println(System.currentTimeMillis());
        Assert.fail();
    }
}

