package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderBoardCtrl {

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    TableColumn<String, SimpleUser> nameColumn;

    @FXML
    TableColumn<Integer, SimpleUser> scoreColumn;

    @FXML
    TableColumn<String, Integer> positionColumn;

    @FXML
    private TextField nameForHighScore;

    @FXML
    private Text displayHighScore;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<SimpleUser> players;
    private TimerTask scheduler;

    @Inject
    public LeaderBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * When the back button is pressed, the splash screen is displayed
     */
    public void back() {
        mainCtrl.showSplash();
        tablePlayers.getItems().clear();
        nameForHighScore.clear();
        displayHighScore.setVisible(false);
    }

    /**
     * Method that gets triggered when a user inputs a given name and presses "ENTER"
     * textField container is provided directly to the right of the leaderboard
     * This computes the highest score associated to the inputted name and prompts what's the highest score for this given name
     * in case the name exists in the leaderboard, "There is no player with this name" if the name does not appear in the leaderboard
     * or "invalid name" if the textField is empty.
     */
    public void obtainHighScore() {

        if (scheduler != null)
            scheduler.cancel();

        displayHighScore.setVisible(true);
        String playerName = nameForHighScore.getText();
        if (isNullOrEmpty(playerName)) {
            displayHighScore.setText("The name you inputted is invalid.");
        } else {
            Optional<String> searchedPlayer = this.players
                    .stream().
                    map(SimpleUser::getName)
                    .filter(name -> playerName.equals(name))
                    .findFirst();
            if (searchedPlayer.isPresent()) {
                Optional<Integer> highScore = this.players
                        .stream().
                        filter(player -> player.getName().equals(playerName))
                        .map(SimpleUser::getScore).
                        max(Integer::compare);
                displayHighScore.setText(playerName + "'s highest score is: " + highScore.get() + " points.");
            } else {
                displayHighScore.setText("There is no player with this name.");
            }
        }

        this.pollHighScore();

    }

    /**
     * Additional method that checks whether the playerName is null or empty
     *
     * @param playerName the String object instance we are interested in
     * @return true: if the inputted name is null or empty or false: otherwise
     */
    public boolean isNullOrEmpty(String playerName) {
        return (playerName == null || playerName.equals(""));
    }

    /**
     * Method that disables the highScore text after 3 seconds using simple polling.
     * If there are more requests sent in the given interval, all previous schedules will get canceled.
     * Therefore, only the last schedule will run at any time (as only one high score prompt should be shown to the user).
     */
    public void pollHighScore() {
        scheduler = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->
                        displayHighScore.setVisible(false)
                );
            }
        };
        new Timer().schedule(scheduler, 3000);
    }


    /**
     * Puts all the players with their scores on the leaderboard table,
     * with the positions that they obtained during the game
     * @param players all the players that are shown on the leaderboard
     */
    public void setTablePlayers(List<SimpleUser> players) {
        this.initialise(players);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        //show player positions
        positionColumn.setCellFactory(col -> {
            TableCell<String, Integer> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null;
                } else {
                    return Integer.toString(cell.getIndex() + 1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));
            return cell;
        });


        //sort players
        var sortedPlayers = players.stream().sorted(Comparator
                        .comparingLong(SimpleUser::getScore).reversed())
                .collect(Collectors.toList());


        // Load players into table
        ObservableList<SimpleUser> data = FXCollections.observableList(sortedPlayers);
        tablePlayers.setItems(data);


        // alternative for loading players
        /*for (SimpleUser simpleUser : sortedPlayers){
            tablePlayers.getItems().add(simpleUser);
        }*/
    }

    /**
     * Method that initialises the list of players sent from the server in setTablePlayers method
     * This also clears the textLabel and makes the high score prompt invisible.
     *
     * @param players list of SimpleUser instances that is saved locally for each Leaderboard scene opened.
     */
    public void initialise(List<SimpleUser> players) {
        this.players = players;
        nameForHighScore.clear();
        displayHighScore.setVisible(false);
    }
}
