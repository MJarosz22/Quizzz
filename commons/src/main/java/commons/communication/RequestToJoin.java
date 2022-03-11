package commons.communication;

import commons.GameInstance;

public class RequestToJoin {

    private String name;
    private int gameType;

    private RequestToJoin(){}

    public RequestToJoin(String name, int gameType) {
        this.name = name;
        if(gameType != GameInstance.SINGLE_PLAYER && gameType != GameInstance.MULTI_PLAYER) throw new IllegalArgumentException();
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        if(gameType != GameInstance.SINGLE_PLAYER && gameType != GameInstance.MULTI_PLAYER) throw new IllegalArgumentException();
        this.gameType = gameType;
    }
}
