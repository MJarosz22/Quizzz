package commons;

import commons.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Test the getQuesiton, setQuesiton and generateQuestion methods (after we finish designing Question class).
public class GameInstanceTest {
    @Test
    public void checkConstructor() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertNotNull(gameInstance);
    }

    @Test
    public void getIdTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(0, gameInstance.getId());
    }

    @Test
    public void setIdTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        gameInstance.setId(100);
        assertEquals(100, gameInstance.getId());
    }

    @Test
    public void getTypeTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(GameInstance.SINGLE_PLAYER, gameInstance.getType());
    }

    @Test
    public void setTypeTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        gameInstance.setType(GameInstance.MULTI_PLAYER);
        assertEquals(GameInstance.MULTI_PLAYER, gameInstance.getType());
    }

    @Test
    public void getPlayersTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertNotNull(gameInstance.getPlayers());
    }

    @Test
    public void setPlayersTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player1 = new Player(1L, "Vlad", gameInstance, "oneCookie");
        Player player2 = new Player(2L, "Petra", gameInstance, "anotherCookie");
        Player player3 = new Player(3L, "Joshua", gameInstance, "lastCookie");

        List<Player> listOfPlayers = new ArrayList<Player>();

        listOfPlayers.add(player1);
        listOfPlayers.add(player2);
        listOfPlayers.add(player3);

        gameInstance.setPlayers(listOfPlayers);

        assertEquals(player1, listOfPlayers.get(0));
        assertEquals(player2, listOfPlayers.get(1));
        assertEquals(player3, listOfPlayers.get(2));
    }


    @Test
    public void equalsHashCode() {
        GameInstance gameInstance1 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        GameInstance gameInstance2 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(gameInstance1, gameInstance2);
        assertEquals(gameInstance1.hashCode(), gameInstance2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        GameInstance gameInstance1 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        GameInstance gameInstance2 = new GameInstance(1, GameInstance.SINGLE_PLAYER);
        assertNotEquals(gameInstance1, gameInstance2);
        assertNotEquals(gameInstance1.hashCode(), gameInstance2.hashCode());
    }

    @Test
    public void hasToString() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        String s = gameInstance.toString();
        assertTrue(s.contains(GameInstance.class.getSimpleName()));
        assertTrue(s.contains("id="));
        assertTrue(s.contains("type="));
        assertTrue(s.contains("players="));
        assertTrue(s.contains("questions="));
    }
}
