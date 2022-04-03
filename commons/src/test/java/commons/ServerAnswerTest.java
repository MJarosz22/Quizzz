package commons;

import commons.player.Player;
import commons.player.SimpleUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerAnswerTest {

    @Test
    void getPlayer() {
        SimpleUser player1 = new SimpleUser("Petra", 1500);
        ServerAnswer sa = new ServerAnswer(1500L,player1);
        assertEquals(sa.getPlayer(), player1);
    }

    @Test
    void setPlayer() {
        SimpleUser player1 = new SimpleUser("A", 1500);
        Player player2 = new Player();
        ServerAnswer sa = new ServerAnswer(1500L,player1);
        sa.setPlayer(player2);
        assertEquals(sa.getPlayer(), player2);

    }

    @Test
    void testEquals() {
        SimpleUser player1 = new SimpleUser("Petra", 1500);
        ServerAnswer sa1 = new ServerAnswer(1500L,player1);
        ServerAnswer sa2 = new ServerAnswer(1500L,player1);
        assertTrue(sa1.equals(sa2));
    }

    @Test
    void testHashCode() {
        SimpleUser player1 = new SimpleUser("Petra", 1500);
        ServerAnswer sa1 = new ServerAnswer(1500L,player1);
        ServerAnswer sa2 = new ServerAnswer(1500L,player1);
        assertEquals(sa1, sa2);
        assertEquals(sa1.hashCode(), sa2.hashCode());
    }

    @Test
    void testToString() {
        SimpleUser player1 = new SimpleUser("Petra", 1500);
        ServerAnswer sa = new ServerAnswer(1500L,player1);
        String string = sa.toString();
        System.out.println(string);
        assertTrue(string.contains("id=0,gameInstanceId=0,name=Petra,score=1500,cookie=<null>"));
        assertTrue(string.contains("answer=1500"));
    }
}