package commons.powerups;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {

    private PowerUp p1;
    private PowerUp p2;
    private PowerUp p3;

    @BeforeEach
    public void initTests() {
        p1 = new TimePU("cookie", "player", 50);
        p2 = new TimePU("cookie", "player", 50);
    }

    @Test
    public void testConstructorTime() {
        assertTrue(p1 instanceof TimePU);
    }

    @Test
    public void setPromptTest() {
        p1.setPrompt("prompt");
        assertEquals("prompt", p1.prompt);
    }


    @Test
    public void getPromptTest() {
        p1.prompt = "prompt";
        assertEquals("prompt", p1.getPrompt());
    }

    @Test
    public void getPlayerCookieTest() {
        assertEquals("cookie", p1.getPlayerCookie());
    }

    @Test
    public void equalsTest() {
        assertTrue(p1.equals(p2));
    }

    @Test
    public void notEqualsTest() {
        p1.prompt = "prompt1";
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

    @Test
    public void getNameTest() {
        p2 = new TimePU("cookie", "Marcin", 50);
        assertEquals("Marcin", p2.getPlayerName());
    }

    @Test
    public void toStringTest() {
        p2 = new TimePU("cookie", "nameTest", 50);
        String s = p2.toString();
        assertTrue(s.contains("TimePU"));
    }
}
