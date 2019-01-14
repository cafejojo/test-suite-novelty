import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyClassTest {
    @Test
    void testFoo() {
        assertEquals("bar", MyClass().foo());
    }
}
