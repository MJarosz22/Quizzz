package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


//TODO REWRITE TEST OF PLAYER
// Modify these tests with correct powerUps entities after the powerUp class is fully designed
public class PlayerTest {
    @Test
    public void checkConstructor() {
        var q = new Player(0, "Vlad", new GameInstance(0, GameInstance.SINGLE_PLAYER), "cookietest");
        assertEquals("Vlad", q.getName());
        assertEquals("cookietest", q.getCookie());
    }

    @Test
    public void equalsHashCode() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        var a = new Player(0, "Marcin", gi, "cookietest");
        var b = new Player(0, "Rafael", gi, "cookietest");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        var a = new Player(0, "Petra", gi, "cookietest");
        var b = new Player(0, "Joshua", gi, "cookietest");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
}
