package commons.powerups;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is a powerUp that removes one incorrect answer
 */
public class AnswerPU extends PowerUp {

    public AnswerPU() {
    }

    public AnswerPU(String playerCookie, String playerName) {
        super(playerCookie, playerName);
        this.prompt = " removed one incorrect answer!";
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
