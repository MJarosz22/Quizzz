package client.scenes.multiplayer;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Answer;
import commons.QuestionHowMuch;
import commons.player.SimpleUser;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class HowMuchCtrl implements QuestionCtrl {

    @FXML
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount, disconnect;

    @FXML
    private AnchorPane emoji;

    @FXML
    private ImageView timerImage, heartPic, cryPic, laughPic, angryPic, glassesPic;

    @FXML
    private Button submit_guess, heart, cry, laugh, angry, glasses;

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

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private GameCtrl gameCtrl;

    private QuestionHowMuch question;

    @Inject
    public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
        try {
            timerImageSource = new Image(new FileInputStream("client/src/main/resources/images/timer.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find timer image.");
        }
    }

    public void init(QuestionHowMuch question) {
        timerImage.setImage(timerImageSource);
        disablePopUp(null);
        player_answer.clear();
        this.question = question;
        questionTitle.setText(question.getTitle());
        questionCount.setText("Question " + question.getNumber() + "/20");
        option4.setText(question.getActivity().getTitle());
        disconnect.setVisible(false);
        progressBar.setProgress(question.getNumber() / 20.0d + 0.05);
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
                int timeLeft = server.getTimeLeft(gameCtrl.getPlayer());
                Platform.runLater(() -> {
                    timer.setText(String.valueOf(Math.round(timeLeft / 1000d)));
                });
            }
        };
        new Timer().scheduleAtFixedRate(scheduler, 0, 100);
    }

    public void disablePopUp(ActionEvent actionEvent) {
        confirmationExit.setVisible(false);
        confirmationExit.setDisable(true);
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

    public void submitAnswer(ActionEvent actionEvent) {
        try {
            gameCtrl.submitAnswer(new Answer(Long.valueOf(player_answer.getText())));
        } catch (NumberFormatException e) {
            gameCtrl.submitAnswer(new Answer(null));
        }
        finally {
            submit_guess.setDisable(true);
        }
    }

    @Override
    public void postQuestion(Answer answer) {
        try {
            CharSequence input = player_answer.getCharacters();
            long number = Long.parseLong(input.toString());
            long correct_number = question.getActivity().getConsumption_in_wh();
            awardPointsQuestionHowMuch(number, correct_number);
            correct_guess.setText("The correct answer is: " + answer.getAnswer());
            correct_guess.setVisible(true);
        } catch (Exception e) {
            player_answer.clear();
            correct_guess.setVisible(true);
            correct_guess.setText("Invalid number. Maybe next time.");
        } finally {
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
     * @param correct_number long value that represents the correct answer to our QuestionHowMuch type of question
     */
    public void awardPointsQuestionHowMuch(long number, long correct_number) {
        if (number == correct_number) {
            gameCtrl.getPlayer().addScore(100);
            server.updatePlayer(gameCtrl.getPlayer());
            score.setText("Your score: " + gameCtrl.getPlayer().getScore());
            points.setText("+100 points");
            points.setVisible(true);
            answer.setText("Correct answer");
            answer.setVisible(true);
        } else {
            if (number <= correct_number + (25 * correct_number) / 100 && number >= correct_number - (25 * correct_number) / 100) {
                gameCtrl.getPlayer().addScore(75);
                server.updatePlayer(gameCtrl.getPlayer());
                score.setText("Your score: " + gameCtrl.getPlayer().getScore());
                points.setText("+75 points");
                points.setVisible(true);
                answer.setText("Almost the correct answer");
                answer.setVisible(true);
            } else {
                if (number <= correct_number + (50 * correct_number) / 100 && number >= correct_number - (50 * correct_number) / 100) {
                    gameCtrl.getPlayer().addScore(50);
                    server.updatePlayer(gameCtrl.getPlayer());
                    score.setText("Your score: " + gameCtrl.getPlayer().getScore());
                    points.setText("+50 points");
                    points.setVisible(true);
                    answer.setText("Not quite the correct answer");
                    answer.setVisible(true);
                } else {
                    if (number <= correct_number + (75 * correct_number) / 100 && number >= correct_number - (75 * correct_number) / 100) {
                        gameCtrl.getPlayer().addScore(25);
                        server.updatePlayer(gameCtrl.getPlayer());
                        score.setText("Your score: " + gameCtrl.getPlayer().getScore());
                        points.setText("+25 points");
                        points.setVisible(true);
                        answer.setText("Pretty far from the correct answer");
                        answer.setVisible(true);
                    } else {
                        points.setText("+0 points");
                        points.setVisible(true);
                        answer.setText("Wrong answer");
                        answer.setVisible(true);
                    }
                }
            }
        }
    }


    @Override
    public void resetUI() {
        correct_guess.setVisible(false);
        player_answer.clear();
//        timer.setText("12000");
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
}
