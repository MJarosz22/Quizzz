package commons.communication;

import commons.GameInstance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestToJoinTest {
    @Test
    public void checkConstructor() {
        RequestToJoin req = new RequestToJoin();
        assertNotNull(req);
    }

    @Test
    public void getNameTest() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        assertEquals("Player", req.getName());
    }

    @Test
    public void setNameTest() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        req.setName("otherPlayer");
        assertEquals("otherPlayer", req.getName());
    }

    @Test
    public void getServerTest() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        assertEquals("Server", req.getServerName());
    }

    @Test
    public void setServerTest() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        req.setServerName("otherServer");
        assertEquals("otherServer", req.getServerName());
    }

    @Test
    public void getGameTypeTest() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        assertEquals(GameInstance.SINGLE_PLAYER, req.getGameType());
    }

    @Test
    public void setGameType() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        req.setGameType(GameInstance.MULTI_PLAYER);
        assertEquals(GameInstance.MULTI_PLAYER, req.getGameType());
    }

    @Test
    public void equalsHashCode() {
        var a = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        var b = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        var b = new RequestToJoin("anotherPlayer", "Server", GameInstance.SINGLE_PLAYER);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        RequestToJoin req = new RequestToJoin("Player", "Server", GameInstance.SINGLE_PLAYER);
        var a = req.toString();
        assertTrue(a.contains(RequestToJoin.class.getSimpleName()));
        assertTrue(a.contains("name="));
        assertTrue(a.contains("Player"));
        assertTrue(a.contains("gameType="));
        assertTrue(a.contains("0"));
    }


}
