package com.github.pihme.jenkinstestbed.module1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ErrorTest {

    @Before
    public void setUp() {
        System.out.println("Error test");
        throw new RuntimeException("Oops, something happeeed");
    }

    @Test
    public void failNever() {
    }
}

