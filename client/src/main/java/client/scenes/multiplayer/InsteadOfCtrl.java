package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Answer;
import commons.QuestionInsteadOf;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.powerups.PowerUp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
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

public class InsteadOfCtrl implements QuestionCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount, disconnect;

    @FXML
    private AnchorPane emoji;

    @FXML
    private ImageView timerImage;

    @FXML
    private RadioButton answer1, answer2, answer3;

    @FXML
    private Button heart, cry, laugh, angry, glasses, powerUp1, powerUp2, powerUp3;

    @FXML
    private ImageView image4, heartPic, cryPic, laughPic, angryPic, glassesPic;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private Image timerImageSource;

    private TimerTask scheduler;

    private int timeReduced;
    private boolean doublePointsPUUsed;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

    private QuestionInsteadOf question;

    private Long  player_answer;

    @Inject
    public InsteadOfCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        this.doublePointsPUUsed = false;
        try {
            timerImageSource = new Image(new FileInputStream("client/src/main/resources/images/timer.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find timer image.");
        }
    }

    /**
     * Initiates the Instead Of question, sets the scene and starts the timer
     *
     * @param question
     */
    public void init(QuestionInsteadOf question) {
        this.question = question;
        this.timeReduced = 0;
        timerImage.setImage(timerImageSource);
        disablePopUp(null);
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option4.setText(question.getActivity().getTitle());
        progressBar.setProgress(question.getNumber() / 20.0d + 0.05);
        disconnect.setVisible(false);
        answer1.setText(question.getAnswers()[0]);
        answer2.setText(question.getAnswers()[1]);
        answer3.setText(question.getAnswers()[2]);
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
        score.setText("Your score: "+ gameCtrl.getPlayer().getScore());
        answer.setVisible(false);
        points.setVisible(false);
        setPowerUps();
        try {
            Image image = new Image(server.getImage(question.getActivity()));
            image4.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find image");
        }
        scheduler = new TimerTask() {
            @Override
            public void run() {
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    if (Math.round((timeLeft) / 1000d) <= 2)
                        powerUp3.setDisable(true);
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    public void answer3Selected(ActionEvent actionEvent) {
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 3));
        player_answer = question.getActivities()[2].getConsumption_in_wh();
    }

    public void answer2Selected(ActionEvent actionEvent) {
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 2));
        player_answer = question.getActivities()[2].getConsumption_in_wh();
    }

    public void answer1Selected(ActionEvent actionEvent) {
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 1));
        player_answer = question.getActivities()[2].getConsumption_in_wh();
    }

    public void disablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(false);
        confirmationExit.setDisable(true);
    }

    /**
     * Use the time reduction powerup
     *
     * @param actionEvent click on the powerUp
     */
    public void decreaseTime(ActionEvent actionEvent) {
        server.useTimePowerup(gameCtrl.getPlayer(), 50);
    }

    /**
     * Use the double points powerup
     *
     * @param actionEvent click on the powerUp
     */
    public void doublePoints(ActionEvent actionEvent) {
        doublePointsPUUsed = true;
        server.usePointsPowerup(gameCtrl.getPlayer());
    }

    /**
     * reduce the time for this player by the given percentage
     *
     * @param percentage
     */
    @Override
    public void reduceTimer(int percentage) {
        scheduler.cancel();
        timeReduced += (server.getTimeLeft(gameCtrl.getPlayer()) - timeReduced) * percentage / 100;
        scheduler = new TimerTask() {

            @Override
            public void run() {
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    timer.setText(String.valueOf(Math.max(Math.round((timeLeft - timeReduced) / 1000d), 0)));
                });
                if (Math.round((timeLeft) / 1000d) <= 2)
                    powerUp3.setDisable(true);
                if (Math.round((timeLeft - timeReduced) / 1000d) <= 0) {
                    Platform.runLater(() -> {
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

    /**
     * Sends the answer to the server and starts a 5-second countdown
     *
     * @param answer
     */
    @Override
    public void postQuestion(Answer answer) {
        if(player_answer != null && player_answer == question.getAnswer()){
            int numberOfPoints = calculatePoints(server.getTimeLeft(gameCtrl.getPlayer()));
            if(doublePointsPUUsed) numberOfPoints = numberOfPoints * 2;
            gameCtrl.getPlayer().addScore(numberOfPoints);
            server.updatePlayer(gameCtrl.getPlayer());
            score.setText("Your score: " + gameCtrl.getPlayer().getScore());
            points.setText("+" + numberOfPoints + " points");
            points.setVisible(true);
            this.answer.setText("Correct answer");
            this.answer.setVisible(true);
        } else{
            points.setText("+0 points");
            points.setVisible(true);
            this.answer.setText("Wrong answer");
            this.answer.setVisible(true);
        }
        switch (answer.getAnswer().intValue()) {
            case 1:
                answer1.setDisable(true);
                answer2.setDisable(true);
                answer3.setDisable(true);
                answer1.setStyle("-fx-background-color: green");
                answer2.setStyle("-fx-background-color: red");
                answer3.setStyle("-fx-background-color: red");
                break;
            case 2:
                answer1.setDisable(true);
                answer2.setDisable(true);
                answer3.setDisable(true);
                answer1.setStyle("-fx-background-color: red");
                answer2.setStyle("-fx-background-color: green");
                answer3.setStyle("-fx-background-color: red");
                break;
            case 3:
                answer1.setDisable(true);
                answer2.setDisable(true);
                answer3.setDisable(true);
                answer1.setStyle("-fx-background-color: red");
                answer2.setStyle("-fx-background-color: red");
                answer3.setStyle("-fx-background-color: green");
                break;
            default:
                System.out.println(answer.getAnswer().intValue());
                throw new IllegalStateException();
        }
        timeReduced = 0;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scheduler.cancel();
                resetUI();
            }
        }, 5000);
    }

    public int calculatePoints(int timeLeft) {
        timeLeft = (int) (timeLeft / 1000d);
        return (timeLeft * 10) / 2;
    }

    @Override
    public void resetUI() {
        enableAnswers();
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("");
        answer1.setSelected(false);
        answer2.setSelected(false);
        answer3.setSelected(false);
    }

    /**
     * Block answers for this player (for example when their time runs out)
     */
    public void disableAnswers() {
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
    }

    /**
     * Enable answers for this player
     */
    public void enableAnswers() {
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
    }

    /**
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
        server.sendEmoji(gameCtrl.getPlayer(), "laugh");
    }


    /**
     * Switch case method to call from Websockets that associates an id with its button and a picture
     * and makes them bold
     *
     * @param id id of button (and image to increase size
     */
    public void emojiSelector(String id) {

        //String currentQType = server.getCurrentQType(server.getLastGIIdMult());
        System.out.println("ID SELECTION BEGINS");
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
                break;
        }
    }

    /**
     * Method that boldens (enlargens) the emoji clicked, then shrinks it back into position
     *
     * @param emojiButton The emoji button to be enlarged
     * @param emojiPic    The corresponding image associated with that button
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
                    Platform.runLater(() -> {
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
     *
     * @param disconnectPlayer
     */
    @Override
    public void showDisconnect(SimpleUser disconnectPlayer) {
        disconnect.setText(disconnectPlayer.getName() + " disconnected");
        disconnect.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> disconnect.setVisible(false));
            }
        }, 5000);
    }

    /**
     * Displays a message when another player uses a powerUp
     *
     * @param powerUp
     */
    public void showPowerUpUsed(PowerUp powerUp) {
        disconnect.setText(powerUp.getPlayerName() + powerUp.getPrompt());
        disconnect.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> disconnect.setVisible(false));
            }
        }, 2000);
    }

    /**
     * Get the powerUps available for this player from server
     * and adjust the powerUp buttons accordingly
     */
    public void setPowerUps() {
        boolean[] powerUps = ((Player) (gameCtrl.getPlayer())).getPowerUps();
        powerUp1.setDisable(!powerUps[0]);
        powerUp2.setDisable(!powerUps[1]);
        powerUp3.setDisable(!powerUps[2]);
    }
}
