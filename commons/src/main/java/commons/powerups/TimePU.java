package commons.powerups;


public class TimePU extends PowerUp {

    public TimePU() {
    }

    private int percentage;

    public TimePU(String playerCookie, int percentage) {
        super(playerCookie);
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getPlayerCookie() {
        return playerCookie;
    }
}
