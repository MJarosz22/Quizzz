package commons.powerups;

public abstract class PowerUp {

    protected String playerCookie;

    protected String prompt;

    public PowerUp() {
    }

    public PowerUp(String playerCookie) {
        this.playerCookie = playerCookie;
    }

    public String getPlayerCookie() {
        return playerCookie;
    }

    public String getPrompt() {
        return this.prompt;
    }
}
