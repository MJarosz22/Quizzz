package commons;

import commons.player.SimpleUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmojiTest {

    @Test
    void emptyConstructorTest(){
        Emoji emoji = new Emoji();
        assertNotNull(emoji);
    }

    @Test
    void getType() {
        Emoji emoji = new Emoji("cry",new SimpleUser());
        assertTrue(emoji.getType().equals("cry"));
    }

    @Test
    void setType() {
        Emoji emoji = new Emoji("cry",new SimpleUser());
        emoji.setType("heart");
        assertTrue(emoji.getType().equals("heart"));
    }

    @Test
    void getPlayer() {
        SimpleUser rafa = new SimpleUser("rafa",0);
        Emoji emoji = new Emoji("cry",rafa);
        assertTrue(emoji.getPlayer().getName().equals("rafa"));
    }

    @Test
    void setPlayer() {
        SimpleUser rafa = new SimpleUser("rafa",0);
        Emoji emoji = new Emoji("cry",rafa);
        emoji.setPlayer(new SimpleUser("vlad",0));
        assertTrue(emoji.getPlayer().getName().equals("vlad"));
    }
}