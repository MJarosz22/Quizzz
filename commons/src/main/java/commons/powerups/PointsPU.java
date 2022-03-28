package commons.powerups;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

/**
 * This is a powerUp that doubles your points if you answer the question that you used the powerUp for correctly
 */
public class PointsPU extends PowerUp {

    public PointsPU() {
    }

    public PointsPU(String playerCookie, String playerName) {
        super(playerCookie, playerName);
        this.prompt = " doubled their points!";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("playerCookie", playerCookie)
                .append("prompt", prompt)
                .append("playerName", playerName)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
