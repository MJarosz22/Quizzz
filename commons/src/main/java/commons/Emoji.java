package commons;

import commons.player.SimpleUser;

public class Emoji {

    private String type;
    private SimpleUser player;

    public Emoji() {
    }

    public Emoji(String type, SimpleUser player) {
        this.type = type;
        this.player = player;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(SimpleUser player) {
        this.player = player;
    }
}

