package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static int persons;
    private boolean sceneChanged;

    @FXML
    private Label labelName;

    @FXML
    private Text personsText;

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    private TableColumn<SimpleUser, String> columnName;

    @Inject
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        persons = 0;
        columnName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
        this.sceneChanged = false;
        startPolling();
    }

    public void startPolling() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable poller = new Runnable() {
            @Override
            public void run() {
                if (mainCtrl.getPlayer() != null) {
                    if (sceneChanged)
                        executor.shutdown();
                    setTablePlayers(ServerUtils.getPlayers(mainCtrl.getPlayer()));
                    changePrompt();
                }
            }
        };


        executor.scheduleAtFixedRate(poller, 0, 1, TimeUnit.SECONDS);
    }


    /**
     * When you press "LEAVE LOBBY" for the multi-player variant of the game, or "BACK"
     * in the singleplayer variant, the player should be disconnected and guided back to the splash screen.
     */
    public void back() {
        SimpleUser player = mainCtrl.getPlayer();
        this.sceneChanged = true;
        server.disconnect(player);
        System.out.println(player.getName() + " disconnected!");
        //decreaseNumberOfPlayers();
        mainCtrl.showSplash();
    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        this.sceneChanged = true;
        //TODO CONNECT TO SERVER
//        mainCtrl.showPlayMode();
    }

    /*
    public void setLabelName(String name) {
        labelName.setText(name);
    }*/

    public void setTablePlayers(List<SimpleUser> players) {
        tablePlayers.setItems(FXCollections.observableList(players));
    }

    public int getPersons() {
        return server.getPlayerList(server.getLastGIIdMult()).size();
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    /**
     * Additional method that decreases the number of players that are currently in the lobby, when a player leaves.
     */
    /*
     public void decreaseNumberOfPlayers() {
     setPersons(getPersons() - 1);
     changePrompt();
     }
     */

    /**
     * Additional method that increases the number of players that are currently in the lobby, when a player joins.
     */

    /*
     public void increaseNumberOfPlayers() {
     setPersons(getPersons() + 1);
     changePrompt();
     }
     */

    /**
     * Additional method that changes the prompt that gets called whenever a player joins/leaves the lobby
     */

    public void changePrompt() {
        if (getPersons() > 1)
            personsText.setText("There are " + getPersons() + " players out of the maximum capacity of 50");
        else
            personsText.setText("There is " + getPersons() + " player out of the maximum capacity of 50");
    }

    /*
     public void setPersonsText(String s) {
     this.personsText.setText(s);
     }
     */


}
