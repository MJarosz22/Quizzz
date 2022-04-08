package client.scenes;

import com.google.inject.Inject;
import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SinglePlayerGameOverCtrl {

    @FXML
    private TableView<SimpleUser> tablePlayers;

    @FXML
    TableColumn<String, SimpleUser> nameColumn;

    @FXML
    TableColumn<Integer, SimpleUser> scoreColumn;

    @FXML
    TableColumn<String, Integer> positionColumn;

    private final MainCtrl mainCtrl;

    @Inject
    public SinglePlayerGameOverCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that is triggered when the user presses 'BACK' button in GameOver screen
     */
    public void back() {
        SinglePlayerCtrl singlePlayerCtrl = mainCtrl.getSinglePlayerCtrl();
        String previousTextField = singlePlayerCtrl.getPlayerName();
        singlePlayerCtrl.setTextField(previousTextField);
        mainCtrl.showSplash();
    }

    public void playAgain() {
        SinglePlayerCtrl singlePlayerCtrl = mainCtrl.getSinglePlayerCtrl();
        String name = singlePlayerCtrl.getPlayerName();
        singlePlayerCtrl.setTextField(name);
        singlePlayerCtrl.play();
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
