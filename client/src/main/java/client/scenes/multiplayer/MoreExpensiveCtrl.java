package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Answer;
import commons.QuestionMoreExpensive;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class MoreExpensiveCtrl implements QuestionCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, correct_guess, questionCount, disconnect;

    @FXML
    private AnchorPane emoji;

    @FXML
    private ImageView timerImage;

    @FXML
    private Button option1Button, option2Button, option3Button, heart, cry, laugh, angry, glasses;


    @FXML
    private ImageView image1, image2, image3, heartPic, cryPic, laughPic, angryPic, glassesPic;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private Image timerImageSource;

    private TimerTask scheduler;

    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private final ServerUtils server;

    private QuestionMoreExpensive question;

    @Inject
    public MoreExpensiveCtrl(MainCtrl mainCtrl, GameCtrl gameCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        this.server = server;
        try {
            timerImageSource = new Image(new FileInputStream("client/src/main/resources/images/timer.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find timer image.");
        }
    }

    public void init(QuestionMoreExpensive question){
        this.question = question;
        timerImage.setImage(timerImageSource);
        disablePopUp(null);
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option1Button.setText(question.getActivities()[0].getTitle());
        option2Button.setText(question.getActivities()[1].getTitle());
        option3Button.setText(question.getActivities()[2].getTitle());
        disconnect.setVisible(false);
        progressBar.setProgress(question.getNumber() / 20.0d + 0.05);
        try {
            Image loadimage1 = new Image(server.getImage(question.getActivities()[0]));
            Image loadimage2 = new Image(server.getImage(question.getActivities()[1]));
            Image loadimage3 = new Image(server.getImage(question.getActivities()[2]));
            image1.setImage(loadimage1);
            image2.setImage(loadimage2);
            image3.setImage(loadimage3);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find image");
        }
        scheduler = new TimerTask() {
            @Override
            public void run() {
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    public void option3Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 3));
    }

    public void option2Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 2));
    }

    public void option1Selected(ActionEvent actionEvent) {
        gameCtrl.submitAnswer(new Answer((long) 1));
    }

    public void disablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(false);
        confirmationExit.setDisable(true);
    }

    public void decreaseTime(ActionEvent actionEvent){
        server.useTimePowerup(gameCtrl.getPlayer(),50);
    }

    @Override
    public void reduceTimer(int percentage){
        scheduler.cancel();
        scheduler = new TimerTask() {
            @Override
            public void run() {
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer())*percentage/100;
                Platform.runLater(() -> {
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
                if(timeLeft==0){
                    Platform.runLater(() ->{
                        disableAnswers();
                    });
                }

            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    public void leaveGame(ActionEvent actionEvent) {
        scheduler.cancel();
        gameCtrl.disconnect();
        mainCtrl.showSplash();
    }

    public void enablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(true);
        confirmationExit.setDisable(false);
        confirmationExit.setStyle("-fx-background-color: #91e4fb; ");
    }

    @Override
    public void postQuestion(Answer answer){
        switch (answer.getAnswer().intValue()) {
            case 1:
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setStyle("-fx-background-color: green");
                option2Button.setStyle("-fx-background-color: red");
                option3Button.setStyle("-fx-background-color: red");
                break;
            case 2:
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setStyle("-fx-background-color: red");
                option2Button.setStyle("-fx-background-color: green");
                option3Button.setStyle("-fx-background-color: red");
                break;
            case 3:
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setStyle("-fx-background-color: red");
                option2Button.setStyle("-fx-background-color: red");
                option3Button.setStyle("-fx-background-color: green");
                break;
            default:
                throw new IllegalStateException();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scheduler.cancel();
                resetUI();
            }
        }, 5000);
    }

    @Override
    public void resetUI() {
        option1Button.setStyle("");
        option2Button.setStyle("");
        option3Button.setStyle("");
        enableAnswers();
    }

    public void disableAnswers(){
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
    }

    public void enableAnswers(){
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
    }

    /**
<<<<<<< client/src/main/java/client/scenes/multiplayer/MoreExpensiveCtrl.java
     * Method to select heart emoji
     */

    public void heartBold() {
        server.sendEmoji(gameCtrl.getPlayer(), "heart");
    }

    /**
     * Method to select glasses emoji
     */

    public void glassesBold() {
        server.sendEmoji(gameCtrl.getPlayer(), "glasses");
    }

    /**
     * Method to select angry emoji
     */

    public void angryBold() {
        server.sendEmoji(gameCtrl.getPlayer(), "angry");
    }

    /**
     * Method to select crying emoji
     */

    public void cryBold() {
        server.sendEmoji(gameCtrl.getPlayer(), "cry");
    }

    /**
     * Method to select laughing emoji
     */

    public void laughBold() {
        System.out.println("laugh should be sent");
        server.sendEmoji(gameCtrl.getPlayer(), "laugh");
    }

    /**
     * Switch case method to call from Websockets that associates an id with its button and a picture
     * and makes them bold
     *
     * @param id id of button (and image to increase size
     */
    public void emojiSelector(String id){
        switch (id) {
            case "heart":
                emojiBold(heart, heartPic);
                break;
            case "glasses":
                emojiBold(glasses, glassesPic);
                break;
            case "angry":
                emojiBold(angry, angryPic);
                break;
            case "cry":
                emojiBold(cry, cryPic);
                break;
            case "laugh":
                emojiBold(laugh, laughPic);
                break;
            default:
                System.out.println("INVALID EMOJI");
        }
    }


    /**
     * Method that boldens (enlargens) the emoji clicked, then shrinks it back into position
     *
     * @param emojiButton The emoji button to be enlarged
     * @param emojiPic The corresponding image associated with that button
     */
    public void emojiBold(Button emojiButton, ImageView emojiPic) {
        Platform.runLater(() -> {
            emojiButton.setStyle("-fx-pref-height: 50; -fx-pref-width: 50; -fx-background-color: transparent; ");
            emojiButton.setLayoutX(emojiButton.getLayoutX() - 10.0);
            emojiButton.setLayoutY(emojiButton.getLayoutY() - 10.0);
            emojiButton.setMouseTransparent(true);
            emojiPic.setFitWidth(50);
            emojiPic.setFitHeight(50);

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(()->{
                        emojiButton.setStyle("-fx-pref-height: 30; -fx-pref-width: 30; -fx-background-color: transparent; ");
                        emojiButton.setLayoutX(emojiButton.getLayoutX() + 10.0);
                        emojiButton.setLayoutY(emojiButton.getLayoutY() + 10.0);
                        emojiButton.setMouseTransparent(false);
                        emojiPic.setFitWidth(30);
                        emojiPic.setFitHeight(30);
                    });
                }
            };
            new Timer().schedule(timerTask, 5000);
        });
    }

    @Override
    public void showEmoji(String type) {
        emojiSelector(type);
    }
    
    /**
     * Displays a message when another player disconnects
     * @param disconnectPlayer
     */
    @Override
    public void showDisconnect(SimpleUser disconnectPlayer) {
        disconnect.setText(disconnectPlayer.getName() + " disconnected");
        disconnect.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> disconnect.setVisible(false));
            }
        }, 5000);
    }
}
