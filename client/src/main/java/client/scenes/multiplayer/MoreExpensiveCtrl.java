package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Answer;
import commons.QuestionMoreExpensive;
import commons.player.Player;
import commons.player.SimpleUser;
import commons.powerups.PowerUp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MoreExpensiveCtrl implements QuestionCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, correct_guess, questionCount, heartText, cryText,
            laughText, angryText, glassesText, disconnect;

    @FXML
    private AnchorPane emoji;

    @FXML
    private ImageView timerImage;

    @FXML
    private Button option1Button, option2Button, option3Button, heart, cry, laugh, angry, glasses, powerUp1, powerUp2, powerUp3;


    @FXML
    private ImageView image1, image2, image3, heartPic, cryPic, laughPic, angryPic, glassesPic;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private Image timerImageSource;

    private TimerTask scheduler;

    private int timeReduced;
    private boolean doublePointsPUUsed;

    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;
    private final ServerUtils server;

    private QuestionMoreExpensive question;

    Long player_answer;

    @Inject
    public MoreExpensiveCtrl(MainCtrl mainCtrl, GameCtrl gameCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        this.server = server;
        this.doublePointsPUUsed = false;
        try {
            timerImageSource = new Image(new FileInputStream("client/src/main/resources/images/timer.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find timer image.");
        }
    }


    /**
     * Method triggered when the current question in the game is of type More Expensive
     * Initiates the More Expensive question, sets the scene and starts the timer
     * @param question the question that is asked
     */
    public void init(QuestionMoreExpensive question) {
        this.question = question;
        this.timeReduced = 0;
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
        progressBar.setProgress(question.getNumber() / 20.0d);
        score.setText("Your score: " + gameCtrl.getPlayer().getScore());
        answer.setVisible(false);
        points.setVisible(false);
        setPowerUps();
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
                    if (Math.round((timeLeft) / 1000d) <= 2)
                        powerUp3.setDisable(true);
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    public void option3Selected(ActionEvent actionEvent) {
        option3Button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        powerUp1.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 3));
        player_answer = question.getActivities()[2].getConsumption_in_wh();
    }

    public void option2Selected(ActionEvent actionEvent) {
        option2Button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        powerUp1.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 2));
        player_answer = question.getActivities()[1].getConsumption_in_wh();
    }

    public void option1Selected(ActionEvent actionEvent) {
        option1Button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        powerUp1.setDisable(true);
        gameCtrl.submitAnswer(new Answer((long) 1));
        player_answer = question.getActivities()[0].getConsumption_in_wh();
    }


    /**
     * The pop-up to confirm the exit from a game is disabled
     */
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
     * Use the remove incorrect answer powerup
     *
     * @param actionEvent click on the powerUp
     */
    public void removeAnswer(ActionEvent actionEvent) {
        Random random = new Random();
        int randomAnswer = random.nextInt(3)+1;
        while(randomAnswer == question.getCorrectAnswer() || (randomAnswer != 1 && randomAnswer != 2 && randomAnswer != 3)) {
            randomAnswer = random.nextInt(3)+1;
        }
        switch (randomAnswer) {
            case 1:
                option1Button.setDisable(true);
                option1Button.setStyle("-fx-background-color: red");
                break;
            case 2:
                option2Button.setDisable(true);
                option2Button.setStyle("-fx-background-color: red");
                break;
            case 3:
                option3Button.setDisable(true);
                option3Button.setStyle("-fx-background-color: red");
                break;
            default:
                throw new IllegalStateException();
        }
        server.useAnswerPowerup(gameCtrl.getPlayer());
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


    /**
     * Method is triggered when the player exists the game
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
     * Method is triggered after all players have submitted their answer
     * The points are awarded to the player, the correct answer is displayed and the power ups are disabled
     * @param answer
     */
    @Override
    public void postQuestion(Answer answer){
        powerUp3.setDisable(true);
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
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setBorder(null);
                option2Button.setBorder(null);
                option3Button.setBorder(null);
                powerUp2.setDisable(true);
                option1Button.setStyle("-fx-background-color: green");
                option2Button.setStyle("-fx-background-color: red");
                option3Button.setStyle("-fx-background-color: red");
                break;
            case 2:
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setBorder(null);
                option2Button.setBorder(null);
                option3Button.setBorder(null);
                powerUp2.setDisable(true);
                option1Button.setStyle("-fx-background-color: red");
                option2Button.setStyle("-fx-background-color: green");
                option3Button.setStyle("-fx-background-color: red");
                break;
            case 3:
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option1Button.setBorder(null);
                option2Button.setBorder(null);
                option3Button.setBorder(null);
                powerUp2.setDisable(true);
                option1Button.setStyle("-fx-background-color: red");
                option2Button.setStyle("-fx-background-color: red");
                option3Button.setStyle("-fx-background-color: green");
                break;
            default:
                throw new IllegalStateException();
        }
        timeReduced = 0;
        doublePointsPUUsed = false;
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
        option1Button.setStyle("-fx-background-color: #91e4fb; ");
        option2Button.setStyle("-fx-background-color: #91e4fb; ");
        option3Button.setStyle("-fx-background-color: #91e4fb; ");
        enableAnswers();
    }


    /**
     * Method to calculate the points according to how fast the player answered
     * @param timeLeft the time left to answer the question
     * @return the points the player receives
     */
    public int calculatePoints(int timeLeft) {
        timeLeft = (int) (timeLeft / 1000d);
        return (timeLeft * 10) / 2;
    }



    /**
     * Block answers for this player (for example when their time runs out)
     */
    public void disableAnswers() {
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
    }

    /**
     * Enable answers for this player
     */
    public void enableAnswers() {
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
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
                emojiBold(glasses, glassesPic, glassesText, player);
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
            text.setText(player.getName().toUpperCase().substring(0, 1));
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
        powerUp1.setDisable(!powerUps[0]);
        powerUp2.setDisable(!powerUps[1]);
        powerUp3.setDisable(!powerUps[2]);
    }
}
