package org.cafejojo.schaapi.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyClassTest {
    @Test
    void testFoo() {
        assertEquals("foo", new MyClass().foo());
    }

    @Test
    void testFooBarBaz() {
        assertEquals("foo", new MyClass().foo());
        assertEquals("bar", new MyClass().bar());
        assertEquals("baz", new MyClass().baz());
    }
}
