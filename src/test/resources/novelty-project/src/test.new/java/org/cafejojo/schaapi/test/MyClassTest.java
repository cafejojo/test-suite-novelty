package org.cafejojo.schaapi.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyClassTest {
    @Test
    void testFooNovel() {
        assertEquals("qux", new MyClass().qux());
    }

    @Test
    void testFooBarBazQuxNovel() {
        assertEquals("foo", new MyClass().foo());
        assertEquals("bar", new MyClass().bar());
        assertEquals("baz", new MyClass().baz());
        assertEquals("qux", new MyClass().qux());
    }

    @Test
    void testFooBarQuxNovel() {
        assertEquals("foo", new MyClass().foo());
        assertEquals("bar", new MyClass().bar());
        assertEquals("qux", new MyClass().qux());
    }

    @Test
    void testFooBazNotNovel() {
        assertEquals("foo", new MyClass().foo());
        assertEquals("baz", new MyClass().baz());
    }
}
