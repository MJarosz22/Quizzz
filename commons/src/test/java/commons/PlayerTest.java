package commons;

import commons.player.Player;
import commons.player.SimpleUser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest {

    @Test
    public void constructorInheritanceTest() {
        Player player = new Player();
        assertNotNull(player);
    }

    @Test
    public void constructorSimpleUserTest() {
        SimpleUser previousPlayer = new SimpleUser(2, "Rafael", 0, "ec04009d98eb9e994d7563480477693c");
        Player player = new Player(previousPlayer);
        boolean[] powerUpsAvailable = new boolean[]{true, true, true};
        assertEquals(0, player.getGameInstanceId());
        assertEquals(0, player.getStatus());
        assertTrue(Arrays.equals(powerUpsAvailable, player.getPowerUps()));
    }

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
        var b = new Player(0, "Marcin", gi, "cookietest");
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

    @Test
    public void getStatusTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Joshua", gi, "ec04009d98eb9e994d7563480477693c");
        assertEquals(0, player.getStatus());

    }

    @Test
    public void setStatusTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Sophie", gi, "ec04009d98eb9e994d7563480477693c");
        player.setStatus(2);
        assertEquals(2, player.getStatus());
    }

    @Test
    public void getPowerUpsTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Petra", gi, "ec04009d98eb9e994d7563480477693c");
        boolean[] powerUpsAvailable = new boolean[]{true, true, true};
        assertTrue(Arrays.equals(powerUpsAvailable, player.getPowerUps()));
    }

    @Test
    public void usePowerUpTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Marcin", gi, "ec04009d98eb9e994d7563480477693c");
        player.usePowerUp(1);
        boolean[] powerUpsAvailable = new boolean[]{true, false, true};
        assertTrue(Arrays.equals(powerUpsAvailable, player.getPowerUps()));

    }

    @Test
    public void nullEqualsTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Vlad", gi, "ec04009d98eb9e994d7563480477693c");
        Player nullPlayer = null;
        assertFalse(player.equals(nullPlayer));
    }

    @Test
    public void toStringTest() {
        GameInstance gi = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player = new Player(0, "Vlad", gi, "ec04009d98eb9e994d7563480477693c");
        String s = player.toString();
        assertTrue(s.contains("status="));
        assertTrue(s.contains("availablePowerUps="));
        assertTrue(s.contains("gameInstance="));
    }
}
