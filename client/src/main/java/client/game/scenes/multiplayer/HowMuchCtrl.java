package client.game.scenes.multiplayer;

import client.game.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.question.Answer;
import commons.question.QuestionHowMuch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class HowMuchCtrl implements QuestionCtrl{

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount;

    @FXML
    private AnchorPane timerImage, emoji;

    @FXML
    private Button submit_guess;

    @FXML
    private TextField player_answer;

    @FXML
    private ImageView image4;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private GameCtrl gameCtrl;

    private QuestionHowMuch question;

    @Inject
    public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }

    public void init(QuestionHowMuch question) {
        disablePopUp(null);
        player_answer.clear();
        this.question = question;
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option4.setText(question.getActivity().getTitle());
        progressBar.setProgress(question.getNumber() / 20.0d + 0.05);
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
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        }, 0, 100);
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

    public void submitAnswer(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer(Long.valueOf(player_answer.getText())));
        //TODO ERROR HANDLING
    }

    @Override
    public void postQuestion(Answer answer) {
        correct_guess.setText("The correct answer is: " + answer.getAnswer());
        correct_guess.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resetUI();
            }
        }, 5000);
    }

    @Override
    public void resetUI() {
        correct_guess.setVisible(false);
        player_answer.clear();
//        timer.setText("12000");
    }
}
