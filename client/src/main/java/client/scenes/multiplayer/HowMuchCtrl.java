package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Answer;
import commons.QuestionHowMuch;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.powerups.PowerUp;
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
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HowMuchCtrl implements QuestionCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount, heartText,
            cryText, laughText, angryText, glassesText, disconnect;

    @FXML
    private AnchorPane emoji;

    @FXML
    private ImageView timerImage, heartPic, cryPic, laughPic, angryPic, glassesPic;

    @FXML
    private Button submit_guess, heart, cry, laugh, angry, glasses, powerUp1, powerUp2, powerUp3;

    @FXML
    private TextField player_answer;

    @FXML
    private ImageView image4;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private Image timerImageSource;

    private TimerTask scheduler;

    private int timeReduced;
    private boolean doublePointsPUUsed;

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private GameCtrl gameCtrl;

    private String timerPath = "/images/timer.png";

    private QuestionHowMuch question;
    private int timeLeft;

    @Inject
    public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        this.doublePointsPUUsed = false;
        try {
            URL absoluteTimerPath = HowMuchCtrl.class.getResource(this.timerPath);
            timerImageSource = new Image(absoluteTimerPath.toString());
        } catch (NullPointerException e) {
            System.out.println("Couldn't find timer image for multiplayer.");
        }
    }


    /**
     * Method triggered when the current question in the game is of type How Much
     * Initiates the How Much question, sets the scene and starts the timer
     * @param question the question that is asked
     */
    public void init(QuestionHowMuch question) {
        timerImage.setImage(timerImageSource);
        disablePopUp(null);
        player_answer.clear();
        this.question = question;
        this.timeReduced = 0;
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option4.setText(question.getActivity().getTitle());
        disconnect.setVisible(false);
        progressBar.setProgress(question.getNumber() / 20.0d);
        setPowerUps();
        score.setText("Your score: " + gameCtrl.getPlayer().getScore());
        answer.setVisible(false);
        points.setVisible(false);
        submit_guess.setDisable(false);
        correct_guess.setVisible(false);
        try {
            Image image = new Image(server.getImage(question.getActivity()));
            image4.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find image");
        }
        scheduler = new TimerTask() {
            @Override
            public void run() {
                timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    if (Math.round((timeLeft) / 1000d) <= 2)
                        powerUp3.setDisable(true);
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    /**
     * The pop-up to confirm the exit from a game is disabled
     */
    public void disablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(false);
        confirmationExit.setDisable(true);
    }

    /**
     * Method is triggered when the player exits the game
     */
    public void leaveGame(ActionEvent actionEvent) {
        scheduler.cancel();
        gameCtrl.disconnect();
        mainCtrl.showSplash();
    }

    /**
     * The pop-up to confirm the exit from a game is shown
     */
    public void enablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(true);
        confirmationExit.setDisable(false);
        confirmationExit.setStyle("-fx-background-color: #91e4fb; ");
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
                 timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
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


    /**
     * The answer of the player is sent to the server
     */
    public void submitAnswer(ActionEvent actionEvent) {
        try {
            gameCtrl.submitAnswer(new Answer(Long.valueOf(player_answer.getText())));
        } catch (NumberFormatException e) {
            gameCtrl.submitAnswer(new Answer(null));
        } finally {
            submit_guess.setDisable(true);
        }
    }

    /**
     * Method is triggered after all players have submitted their answer
     * The points are awarded to the player, the correct answer is displayed and the power ups are disabled
     * @param ans the answer that was sent by the player, or "null" if the player did not answer that specific question
     */
    @Override
    public void postQuestion(Answer ans) {
        powerUp2.setDisable(true);
        powerUp3.setDisable(true);
        submit_guess.setDisable(true); // If an answer was not submitted already.
        timeReduced = 0;
        try {
            CharSequence input = player_answer.getCharacters();
            long number = Long.parseLong(input.toString());
            long correct_number = question.getActivity().getConsumption_in_wh();
            awardPointsQuestionHowMuch(number, correct_number);
        } catch (Exception e) {
            points.setText("+0 points");
            points.setVisible(true);
            answer.setText("Wrong answer");
            answer.setVisible(true);
        } finally {
            correct_guess.setText("The correct answer is: " + ans.getAnswer());
            correct_guess.setVisible(true);
            doublePointsPUUsed = false;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    scheduler.cancel();
                    resetUI();
                }
            }, 5000);
        }
    }

    /**
     * Additional method awards 100,75,50,25 or 0 points to a player, depending on how close he/she was to
     * the correct answer, on behalf of our chosen strategy for this type of question.
     *
     * @param number         long value that represents the number inputted by the player.
     * @param correct_number long value that represents the correct answer to our QuestionHowMuch type of question.
     */
    public void awardPointsQuestionHowMuch(long number, long correct_number) {
        int scoreAdd = 0;
        if (number == correct_number) {
            scoreAdd = 100;
            answer.setText("Correct answer");
        } else {
            if (number <= correct_number + (25 * correct_number) / 100 && number >= correct_number - (25 * correct_number) / 100) {
                scoreAdd = 75;
                answer.setText("Almost the correct answer");
            } else {
                if (number <= correct_number + (50 * correct_number) / 100 && number >= correct_number - (50 * correct_number) / 100) {
                    scoreAdd = 50;
                    answer.setText("Not quite the correct answer");
                } else {
                    if (number <= correct_number + (75 * correct_number) / 100 && number >= correct_number - (75 * correct_number) / 100) {
                        scoreAdd = 25;
                        answer.setText("Pretty far from the correct answer");
                    } else {
                        scoreAdd = 0;
                        answer.setText("Wrong answer");
                    }
                }
            }
        }
        if(this.doublePointsPUUsed) scoreAdd = scoreAdd * 2;
        gameCtrl.getPlayer().addScore(scoreAdd);
        server.updatePlayer(gameCtrl.getPlayer());
        score.setText("Your score: " + gameCtrl.getPlayer().getScore());
        points.setVisible(true);
        points.setText("+" + scoreAdd + " points");
        answer.setVisible(true);
    }


    @Override
    public void resetUI() {
        correct_guess.setVisible(false);
        player_answer.clear();
        enableAnswers();
    }

    /**
     * Block answers for this player (for example when their time runs out)
     */
    public void disableAnswers() {
        this.submit_guess.setDisable(true);
    }

    /**
     * Enable answers for this player
     */
    public void enableAnswers() {
        this.submit_guess.setDisable(false);
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
    public void emojiSelector(String id, SimpleUser player) throws Exception {


            switch (id) {
                case "heart":
                    emojiBold(heart, heartPic, heartText, player);
                    break;
                case "glasses":
                    emojiBold(glasses, glassesPic,  glassesText, player);
                    break;
                case "angry":
                    emojiBold(angry, angryPic, angryText, player);
                    break;
                case "cry":
                    emojiBold(cry, cryPic, cryText, player);
                    break;
                case "laugh":
                    emojiBold(laugh, laughPic, laughText, player);
                    break;
                default:
                    throw new Exception("Invalid emoji");
            }
        }

    /**
     * Method that boldens (enlargens) the emoji clicked, then shrinks it back into position
     *
     * @param emojiButton The emoji button to be enlarged
     * @param emojiPic    The corresponding image associated with that button
     */
    public void emojiBold(Button emojiButton, ImageView emojiPic, Text text, SimpleUser player) {
        Platform.runLater(() -> {
            emojiButton.setStyle("-fx-pref-height: 50; -fx-pref-width: 50; -fx-background-color: transparent; ");
            emojiButton.setLayoutX(emojiButton.getLayoutX() - 10.0);
            emojiButton.setLayoutY(emojiButton.getLayoutY() - 10.0);
            emojiButton.setMouseTransparent(true);
            emojiPic.setFitWidth(50);
            emojiPic.setFitHeight(50);
            text.setText(player.getName().toUpperCase().substring(0,1));
            text.setVisible(true);

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
                        text.setVisible(false);
                    });
                }
            };
            new Timer().schedule(timerTask, 5000);
        });
    }

    @Override
    public void showEmoji(String type, SimpleUser player) {
        try{
            emojiSelector(type, player);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
        powerUp1.setDisable(true);
        powerUp2.setDisable(!powerUps[1]);
        powerUp3.setDisable(!powerUps[2]);
    }
}
