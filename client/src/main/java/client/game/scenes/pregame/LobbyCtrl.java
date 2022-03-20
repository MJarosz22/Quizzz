package client.game.scenes.pregame;

import client.game.scenes.MainCtrl;
import client.game.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private static int persons;

    @FXML
    private Label labelName;

    @FXML
    private Text personsText;

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    private TableColumn<SimpleUser, String> columnName;

    @Inject
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        persons = 0;
        columnName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
    }

    /**
     * When you press "LEAVE LOBBY" for the multi-player variant of the game, or "BACK"
     * in the singleplayer variant, the player should be disconnected and guided back to the splash screen.
     */
    public void back() {
        SimpleUser player = gameCtrl.getPlayer();
        server.disconnect(player);
        System.out.println(player.getName() + " disconnected!");
        //decreaseNumberOfPlayers();
        mainCtrl.showSplash();
    }

    public void init() {
        Platform.runLater(()->{
            List<SimpleUser> players = server.getPlayers(gameCtrl.getPlayer());
            setTablePlayers(players);
            updatePlayers(players);
        });
    }

    public void updatePlayers(List<SimpleUser> players) {
        persons = players.size();
        setTablePlayers(players);
        changePrompt();

    }

    // To be added when making the main game scene, in order for the player to play
    public void play() {
        server.startGame(gameCtrl.getPlayer());
        System.out.println("PLAYING");
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
        if (persons > 1)
            personsText.setText("There are " + persons + " players out of the maximum capacity of 50");
        else
            personsText.setText("There is " + persons + " player out of the maximum capacity of 50");
    }



    /*
     public void setPersonsText(String s) {
     this.personsText.setText(s);
     }
     */


    public void setCountdown(int time) {
        labelName.setText(String.valueOf(time));
    }

}
