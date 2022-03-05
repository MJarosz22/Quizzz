package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "Player")
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "score")
    private int score;

    @Column(name = "status")
    private int status;

    @Column(name = "powerUps")
    private ArrayList<PowerUp> powerUpUsed;

    public Player() {
        //object mapping
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.status = 0;
        this.powerUpUsed = new ArrayList<>();
    }

    public Player(String name, int score, int status, ArrayList<PowerUp> powerUp) {
        this.name = name;
        this.score = score;
        this.status = status;
        this.powerUpUsed = powerUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
