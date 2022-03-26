package commons.powerups;


public class TimePU extends PowerUp {

    private int percentage;

    public TimePU() {
    }

    public TimePU(String playerCookie, int percentage) {
        super(playerCookie);
        this.percentage = percentage;
        this.prompt = " reduced your time!";
    }

    public int getPercentage() {
        return percentage;
    }

}
