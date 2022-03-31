package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private static int persons;
    private boolean sceneChanged;

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


    public void init() {
        timerImage.setImage(timerImageSource);
        Platform.runLater(() -> {
            timer.setVisible(false);
            timerImage.setVisible(false);
            List<SimpleUser> players = server.getPlayers(gameCtrl.getPlayer());
            updatePlayers(players);
        });
    }

    public void initialize() {
//        persons = 0;
        columnName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));
//        this.sceneChanged = false;
//        startPolling();
    }

    public void startPolling() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable poller = new Runnable() {
            @Override
            public void run() {
                if (gameCtrl.getPlayer() != null) {
                    if (sceneChanged)
                        executor.shutdown();
                    setTablePlayers(server.getPlayers(gameCtrl.getPlayer()));
                    changePrompt();
                }
            }
        };


        executor.scheduleAtFixedRate(poller, 0, 1, TimeUnit.SECONDS);
    }

    public void updatePlayers(List<SimpleUser> players) {
        persons = players.size();
        setTablePlayers(players);
        changePrompt();
    }

    /**
     * When you press "LEAVE LOBBY" for the multi-player variant of the game, or "BACK"
     * in the singleplayer variant, the player should be disconnected and guided back to the splash screen.
     */
    public void back() {
        SimpleUser player = gameCtrl.getPlayer();
        this.sceneChanged = true;
        gameCtrl.disconnect();
        // if (server.disconnect(player))
        System.out.println(player.getName() + " disconnected!");
        //decreaseNumberOfPlayers();
        mainCtrl.showSplash();
    }

    public void play() {
        this.sceneChanged = true;
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

    public void setCountdown(int time) {
        timerImage.setVisible(true);
        timer.setVisible(true);
        timer.setText(String.valueOf(time));
    }

}
