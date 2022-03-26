package commons.powerups;


public class TimePU extends PowerUp {

    public TimePU(){}

    private String playerCookie;

    private int percentage;



    public TimePU(int percentage, String playerCookie){
        this.percentage = percentage;
        this.playerCookie=playerCookie;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getPlayerCookie() {
        return playerCookie;
    }
}
