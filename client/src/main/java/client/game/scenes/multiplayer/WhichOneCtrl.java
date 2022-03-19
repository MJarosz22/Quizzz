package client.game.scenes.multiplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class WhichOneCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount;

    @FXML
    private AnchorPane timerImage, emoji;

    @FXML
    private TextField player_answer;

    @FXML
    private RadioButton answer1, answer2, answer3;

    @FXML
    private ImageView image1, image2, image3, image4;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;


    public void leaveGame(ActionEvent actionEvent) {
    }

    public void enablePopUp(ActionEvent actionEvent) {
    }

    public void disablePopUp(ActionEvent actionEvent) {
    }

    public void answer3Selected(ActionEvent actionEvent) {
    }

    public void answer2Selected(ActionEvent actionEvent) {
    }

    public void answer1Selected(ActionEvent actionEvent) {
    }
}
