package commons.player;

import commons.GameInstance;
import commons.PowerUp;

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
}