package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;

public class LobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private static int persons;

    @FXML
    private Label timer;

    @FXML
    private Text personsText;

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    private TableColumn<SimpleUser, String> columnName;

    @FXML
    private ImageView timerImage;

    @FXML
    private Button start;

    private String timerPath = "/images/timer.png";

    private Image timerImageSource;

    @Inject
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        try {
            URL url = LobbyCtrl.class.getResource(this.timerPath);
            timerImageSource = new Image(url.toString());
        } catch (Exception e) {
            System.out.println("Couldn't find timer image for lobby scene.");
        }
    }


    /**
     * Called whenever a player joins a specific gameInstance, initialises the Lobby screen that is shown to this player.
     * Displays the list of players that are waiting in the same lobby and enables the "PLAY" button if the minimum number of
     * players is fulfilled (currently chosen to be 3).
     */
    public void init() {
        Platform.runLater(() -> {
            timerImage.setImage(timerImageSource);
            timer.setVisible(false);
            timerImage.setVisible(false);
            List<SimpleUser> players = server.getPlayers(gameCtrl.getPlayer());
            updatePlayers(players);
            start.setDisable(persons < 2);
        });
    }

    public void initialize() {
        columnName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
    }

    /**
     * Updates the list of players that are currently waiting in a specific Lobby.
     * Enables the "PLAY" button when at least 3 players are waiting in the same waiting room.
     * NOTE that is method is synchronised for all players in a specific gameInstance using WebSockets.
     *
     * @param players list of SimpleUser instances that provides information about each player that is currently waiting in a specific Lobby
     */
    public void updatePlayers(List<SimpleUser> players) {
        persons = players.size();
        setTablePlayers(players);
        changePrompt();
        start.setDisable(persons < 2);
    }

    /**
     * When you press "LEAVE LOBBY" for the multi-player variant of the game, or "BACK"
     * in the singlePlayer variant, the player should be disconnected and guided back to the splash screen.
     */
    public void back() {
        gameCtrl.disconnect();
        mainCtrl.showSplash();
    }

    /**
     * Method that gets triggered when a player presses the "PLAY" button
     */
    public void play() {
        server.startGame(gameCtrl.getPlayer());
    }

    public void setTablePlayers(List<SimpleUser> players) {
        tablePlayers.setItems(FXCollections.observableList(players));
    }

    public int getPersons() {
        return server.connectedPlayers(server.getLastGIIdMult()).size();
    }


    /**
     * Additional method that changes the prompt that gets called whenever a player joins/leaves the lobby
     */
    public void changePrompt() {
        if (getPersons() > 1)
            personsText.setText("There are currently " + persons + " players waiting");
        else
            personsText.setText("There is currently " + persons + " player waiting");
    }

    /**
     * Additional method that starts the 5-seconds countdown that is shown to each player in the top-right corner.
     * This also enables the timer image directly to the left of the timer.
     * NOTE that this method is synchronised for all players in a specific gameInstaance by using WebSockets
     * It is triggered when one player from a lobby presses the "PLAY" button.
     *
     * @param time integer value that goes from 5 to 0, representing how many seconds are there before the actual game will start.
     */
    public void setCountdown(int time) {
        start.setDisable(true);
        timerImage.setVisible(true);
        timer.setVisible(true);
        timer.setText(String.valueOf(time));
    }

}
