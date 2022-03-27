package commons.powerups;

import java.util.Objects;

/**
 * This is a powerUp that reduces time for all other players
 * In addition to standard powerUp properties, it stores
 * the percentage the time should be reduced by
 */
public class TimePU extends PowerUp {

    protected int percentage;

    public TimePU() {
    }

    public TimePU(String playerCookie, String playerName, int percentage) {
        super(playerCookie, playerName);
        this.percentage = percentage;
        this.prompt = " reduced your time!";
    }

    public int getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "TimePU{" +
                "percentage=" + percentage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TimePU timePU = (TimePU) o;
        return percentage == timePU.percentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), percentage);
    }
}
