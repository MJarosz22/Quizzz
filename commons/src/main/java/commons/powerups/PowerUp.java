package commons.powerups;

public abstract class PowerUp {

    protected String playerCookie;

    public PowerUp() {
    }

    public PowerUp(String playerCookie) {
        this.playerCookie = playerCookie;
    }
}
