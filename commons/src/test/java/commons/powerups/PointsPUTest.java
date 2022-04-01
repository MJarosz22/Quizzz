package commons.powerups;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointsPUTest {

    private PointsPU p1;
    private PointsPU p2;

    @BeforeEach
    public void initTests() {
        p1 = new PointsPU("cookie", "player");
        p2 = new PointsPU("cookie", "player");
    }

    @Test
    public void emptyConstructorTest() {
        PointsPU p3 = new PointsPU();
        assertNotNull(p3);
    }

    @Test
    public void equalsTest() {
        assertTrue(p1.equals(p2));
    }

    @Test
    public void equalsHashCode() {
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        p1.setPrompt("otherPrompt");
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void toStringTest() {
        String s = p1.toString();
        assertTrue(s.contains("playerCookie="));
        assertTrue(s.contains("prompt="));
        assertTrue(s.contains("playerName="));
    }
}
