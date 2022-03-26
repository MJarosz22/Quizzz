package commons.powerups;

import java.util.Objects;

/**
 * Abstract base class for all the powerUps.
 * Stores the cookie of player who used the powerup
 * and the prompt that should be shown when the powerup is used
 */
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

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "PowerUp{" +
                "playerCookie='" + playerCookie + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerUp powerUp = (PowerUp) o;
        return Objects.equals(playerCookie, powerUp.playerCookie) && Objects.equals(prompt, powerUp.prompt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerCookie, prompt);
    }
}
