package commons;

import commons.player.SimpleUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleUserTest {


    @Test
    public void emptyConstructorTest() {
        SimpleUser player = new SimpleUser();
        assertNotNull(player);
    }

    @Test
    public void constructorTest() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        assertNotNull(player);
    }

    @Test
    public void checkConstructor() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        assertNotNull(player);
    }

    @Test
    public void equalsHashCode() {
        SimpleUser p1 = new SimpleUser(0, "Marcin", 0, "ec04009d98eb9e994d7563480477693c");
        SimpleUser p2 = new SimpleUser(0, "Marcin", 0, "ec04009d98eb9e994d7563480477693c");
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        SimpleUser p1 = new SimpleUser(0, "Petra", 0, "first-cookie");
        SimpleUser p2 = new SimpleUser(0, "Joshua", 0, "second-cookie");
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void getNameTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        assertEquals("Bob", player.getName());
    }

    @Test
    public void setNameTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setName("Mark");
        assertEquals("Mark", player.getName());
    }

    @Test
    public void getIdTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        assertEquals(2, player.getId());
    }

    @Test
    public void setIdTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setId(15);
        assertEquals(15, player.getId());
    }

    @Test
    // As the score is not attributed to the SimplePlayer game instance in the constructor, we are not able to test
    // the getter and setter separately.
    public void ScoreTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setScore(150);
        assertEquals(150, player.getScore());
    }

    @Test
    public void addScoreTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setScore(150);
        player.addScore(75);
        assertEquals(225, player.getScore());
    }

    @Test
    public void getCookieTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        assertEquals("ec04009d98eb9e994d7563480477693c", player.getCookie());
    }

    @Test
    public void setCookieTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setCookie("fb12314d82ef1e02423jf12390dsajf09dsfj");
        assertEquals("fb12314d82ef1e02423jf12390dsajf09dsfj", player.getCookie());
    }

    @Test
    public void getGameInstanceIdTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        assertEquals(0, player.getGameInstanceId());
    }

    @Test
    public void setGameInstanceIdTest() {
        SimpleUser player = new SimpleUser(2, "Bob", 0, "ec04009d98eb9e994d7563480477693c");
        player.setGameInstanceId(12);
        assertEquals(12, player.getGameInstanceId());
    }

    @Test
    public void toStringTest() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        String s = player.toString();
        assertTrue(s.contains("id="));
        assertTrue(s.contains("gameInstanceId="));
        assertTrue(s.contains("name="));
        assertTrue(s.contains("score="));
        assertTrue(s.contains("cookie="));
    }

    @Test
    public void sameEqualsTest() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        SimpleUser newPlayer = player;
        assertEquals(player, newPlayer);
    }

    @Test
    public void nullEqualsTest() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        SimpleUser newPlayer = null;
        assertFalse(player.equals(newPlayer));
    }

    @Test
    public void unsafeTest() {
        SimpleUser player = new SimpleUser(2, "Vlad", 0, "ec04009d98eb9e994d7563480477693c");
        SimpleUser newPlayer = player.unsafe();
        assertEquals(2, newPlayer.getId());
        assertEquals("Vlad", newPlayer.getName());
        assertEquals(0, newPlayer.getGameInstanceId());
    }

}
