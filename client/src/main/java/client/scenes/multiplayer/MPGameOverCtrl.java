package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.player.SimpleUser;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MPGameOverCtrl {

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    TableColumn<String, SimpleUser> nameColumn;

    @FXML
    TableColumn<Integer, SimpleUser> scoreColumn;

    @FXML
    TableColumn<String, Integer> positionColumn;

    @FXML
    Button play_again;

    @FXML
    Text game_over;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    @Inject
    public MPGameOverCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    /**
     * Method is triggered after 10 and 20 questions, respectively
     * It shows the leaderboard with the players that are still in the game
     * @param players the players that are still in the game
     */
    public void init(List<SimpleUser> players) {
        if(players.get(players.size() - 1).getName().equals("SENTINEL") && players.get(players.size() - 1).getScore() == -1){
            players.remove(players.get(players.size() -1 ));
            play_again.setDisable(true);
            play_again.setVisible(false);
            game_over.setVisible(false);
        } else {
            gameCtrl.disconnect();
            play_again.setDisable(false);
            play_again.setVisible(true);
            game_over.setVisible(true);
        }
        setTablePlayers(players);
    }

    /**
     * Method that is triggered when the user presses 'PLAY AGAIN' button in GameOver screen
     */
    public void back() {
        mainCtrl.showSplash();
    }


    /**
     * Puts all the players with their scores on the leaderboard table,
     * with the positions that they obtained during the game
     * @param players all the players that are shown on the leaderboard
     */
    public void setTablePlayers(List<SimpleUser> players) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        //show player positions
        positionColumn.setCellFactory(col -> {
            TableCell<String,Integer> cell = new TableCell<>();
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
}


