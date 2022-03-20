package client.game.scenes.multiplayer;

import client.game.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.question.Answer;
import commons.question.QuestionWhichOne;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class WhichOneCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount;

    @FXML
    private AnchorPane timerImage, emoji;

    @FXML
    private RadioButton answer1, answer2, answer3;

    @FXML
    private ImageView image4;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private QuestionWhichOne question;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    @Inject
    public WhichOneCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    public void init(QuestionWhichOne question){
        this.question = question;
        disablePopUp(null);
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option4.setText(question.getActivity().getTitle());
        progressBar.setProgress(question.getNumber() / 20.0d + 0.05);
        answer1.setText(String.valueOf(question.getAnswers()[0]));
        answer2.setText(String.valueOf(question.getAnswers()[1]));
        answer3.setText(String.valueOf(question.getAnswers()[2]));
        try {
            Image image = new Image(server.getImage(question.getActivity()));
            image4.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find image");
        }
        Timer scheduler = new Timer();
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    timer.setText(String.valueOf(timeLeft));
                });
            }
        }, 0, 100);
    }

    public void answer3Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 3));
    }

    public void answer2Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 2));
    }

    public void answer1Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 1));
    }

    public void disablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(false);
        confirmationExit.setDisable(true);
    }

    public void leaveGame(ActionEvent actionEvent) {
        gameCtrl.disconnect();
        mainCtrl.showSplash();
    }

    public void enablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(true);
        confirmationExit.setDisable(false);
        confirmationExit.setStyle("-fx-background-color: #91e4fb; ");
    }

}
