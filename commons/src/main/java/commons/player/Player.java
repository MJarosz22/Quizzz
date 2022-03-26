package commons.player;

import commons.GameInstance;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


//@Entity
//@Table(name = "Player")
public class Player extends SimpleUser {

    //@Column(name = "status")
    private int status;

    //@Column(name = "powerUps")
    private boolean[] availablePowerUps;

    private GameInstance gameInstance;

    public Player() {
        super();
        //object mapping
    }

    public Player(long id, String name, GameInstance gameInstance, String cookie) {
        super(id, name, gameInstance.getId(), cookie);
        this.status = 0;
        resetPowerUps();
        this.gameInstance = gameInstance;
    }

    public Player(SimpleUser su) {
        super(su.getId(), su.getName(), su.getGameInstanceId(), su.getCookie());
        this.status = 0;
        resetPowerUps();
        this.gameInstance = gameInstance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean[] getPowerUps() {
        return this.availablePowerUps;
    }

    public void resetPowerUps() {
        this.availablePowerUps = new boolean[]{true, true, true};
    }

    public void usePowerUp(int powerUpIndex) {
        this.availablePowerUps[powerUpIndex] = false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(super.toString())
                .append("status", status)
                .append("availablePowerUps", availablePowerUps)
                .append("gameInstance", gameInstance)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(status, player.status).append(availablePowerUps, player.availablePowerUps)
                .append(gameInstance, player.gameInstance).append(getCookie(), player.getCookie()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(status).append(availablePowerUps).append(gameInstance).append(getCookie()).toHashCode();
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
