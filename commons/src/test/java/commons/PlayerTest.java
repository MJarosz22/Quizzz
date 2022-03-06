package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Modify these tests with correct powerUps entities after the powerUp class is fully designed
public class PlayerTest {
    @Test
    public void checkConstructor() {
        var q = new Player("Vlad", 120, 12, new ArrayList<>());
        assertEquals("Vlad", q.getName());
        assertEquals(120, q.getScore());
        assertEquals(12, q.getStatus());
    }

    @Test
    public void equalsHashCode() {
        var a = new Player("Vlad", 120, 12, new ArrayList<>());
        var b = new Player("Vlad", 120, 12, new ArrayList<>());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Player("Joshua", 120, 12, new ArrayList<>());
        var b = new Player("Petra", 120, 12, new ArrayList<>());
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var q = new Player("Sophie", 120, 12, new ArrayList<>()).toString();
        assertEquals("Player{name='Sophie', score=120, status=12, powerUp=[], id=0}", q);
    }
}
