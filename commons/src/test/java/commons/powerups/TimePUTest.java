package commons.powerups;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimePUTest {

    private TimePU p1;
    private TimePU p2;

    @BeforeEach
    public void initTests() {
        p1 = new TimePU("cookie", 50);
        p2 = new TimePU("cookie", 50);
    }

    @Test
    public void getPercentageTest() {
        assertEquals(50, p1.getPercentage());
    }

    @Test
    public void equalsTest() {
        assertTrue(p1.equals(p2));
    }

    @Test
    public void notEqualsTest() {
        p1.percentage = 60;
        assertFalse(p1.equals(p2));
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
}
