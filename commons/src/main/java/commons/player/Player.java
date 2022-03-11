package commons.player;

import commons.GameInstance;
import commons.PowerUp;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import java.util.ArrayList;

//@Entity
//@Table(name = "Player")
public class Player extends SimpleUser {

    public static final int IN_LOBBY = 0;
    public static final int NOT_ANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int DISCONNECTED = 3;

    @Column(name = "status")
    private int status;

    @Column(name = "powerUps")
    private ArrayList<PowerUp> powerUpUsed;

    private GameInstance gameInstance;

    public Player() {
        super();
        //object mapping
    }

    public Player(long id, String name, GameInstance gameInstance, String cookie) {
        super(id, name, gameInstance.getId(), cookie);
        this.status = 0;
        this.powerUpUsed = new ArrayList<>();
        this.gameInstance = gameInstance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<PowerUp> getPowerUp() {
        return powerUpUsed;
    }

    public void setPowerUps(ArrayList<PowerUp> powerUp) {
        this.powerUpUsed = powerUp;
    }

    public void addPowerUp(PowerUp powerUp) {
        this.powerUpUsed.add(powerUp);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(super.toString())
                .append("status", status)
                .append("powerUpUsed", powerUpUsed)
                .append("gameInstance", gameInstance)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(status, player.status).append(powerUpUsed, player.powerUpUsed)
                .append(gameInstance, player.gameInstance).append(getCookie(), player.getCookie()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(status).append(powerUpUsed).append(gameInstance).append(getCookie()).toHashCode();
    }

    /**
     * Makes Simple user from player, used for communication between client and server (to prevent unnecessary info sharing)
     *
     * @return SimpleUser from this Player
     */
    public SimpleUser toSimpleUser() {
        return new SimpleUser(getId(), getName(), gameInstance.getId(), getCookie());
    }
}
