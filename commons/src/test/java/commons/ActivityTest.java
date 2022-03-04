package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityTest {

    @Test
    public void checkConstructor() {
        var q = new Activity("Activity-ID", "00/test.png", "Title", 6, "q");
        assertEquals("Title", q.title);
        assertEquals(6, q.consumption_in_wh);
        assertEquals("q", q.source);
    }

    @Test
    public void equalsHashCode() {
        var a = new Activity("Activity-ID", "00/test.png", "Title", 6, "q");
        var b = new Activity("Activity-ID", "00/test.png", "Title", 6, "q");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Activity("Activity-ID", "00/test.png", "Title", 6, "q");
        var b = new Activity("Activity-D", "00/test.png", "Title", 6, "q");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var q = new Activity("Activity-ID", "00/test.png", "Title", 6, "q").toString();
        assertTrue(q.contains(Activity.class.getSimpleName()));
        assertTrue(q.contains("title="));
        assertTrue(q.contains("Title"));
        assertTrue(q.contains("6"));
    }
}
