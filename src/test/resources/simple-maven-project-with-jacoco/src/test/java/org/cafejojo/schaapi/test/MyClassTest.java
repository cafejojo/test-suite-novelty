package org.cafejojo.schaapi.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyClassTest {
    @Test
    void testFoo() {
        assertEquals("bar", new MyClass().foo());
    }

    @Test
    void testBar() {
        assertEquals("baz", new MyClass().bar());
    }
}
