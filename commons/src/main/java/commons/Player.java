package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

//@Entity
//@Table(name = "Player")
public class Player extends SimpleUser{

    public static final int IN_LOBBY = 0;
    public static final int NOT_ANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int DISCONNECTED = 3;

    @Column(name = "status")
    public int status;

    @Column(name = "powerUps")
    public ArrayList<PowerUp> powerUpUsed;

    public GameInstance gameInstance;

    public String cookie;

    public Player() {
        super();
        //object mapping
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.status = 0;
        this.powerUpUsed = new ArrayList<>();

    }

    public Player(long id, String name, GameInstance gameInstance, String cookie) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.status = 0;
        this.powerUpUsed = new ArrayList<>();
        this.gameInstance = gameInstance;
        this.cookie = cookie;
    }

    public Player(String name, int score, int status, ArrayList<PowerUp> powerUp) {
        this.name = name;
        this.score = score;
        this.status = status;
        this.powerUpUsed = powerUp;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return score == player.score && status == player.status && id == player.id && Objects.equals(name, player.name)
                && Objects.equals(powerUpUsed, player.powerUpUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score, status, powerUpUsed, id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", status=" + status +
                ", powerUp=" + powerUpUsed +
                ", id=" + id +
                '}';
    }

    public SimpleUser toSimpleUser(){
        return new SimpleUser(name, id, score, gameInstance.id, cookie);
    }

}
