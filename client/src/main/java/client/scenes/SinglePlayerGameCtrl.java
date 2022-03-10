package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//Note that in the future, we can make this controller and its scene suitable for multiplayer games as well
public class SinglePlayerGameCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final String correctEmojiPath = "client/src/main/resources/images/correct-answer.png";
    private final String wrongEmojiPath = "client/src/main/resources/images/wrong-answer.png";

    private SimpleUser player;

    private GameInstance currentGame;
    private Queue<Question> gameQuestions = new LinkedList<>();
    private Question currentQuestion;


    int temporaryCounter;

    @FXML
    private Text questionTitle;

    @FXML
    private Text timer;

    @FXML
    private Text score;

    @FXML
    private Text points;

    @FXML
    private Text answer;

    @FXML
    private AnchorPane emoji;

    @FXML
    private Button option1Button;

    @FXML
    private Button option2Button;

    @FXML
    private Button option3Button;

    @FXML
    private Button correct_answer;

    @FXML
    private  Button wrong_answer1;

    @FXML
    private  Button wrong_answer2;

    @FXML
    private  Button wrong_answer3;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private Text questionCount;

    @FXML
    private ProgressBar progressBar;


    @Inject
    public SinglePlayerGameCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showSplash();
    }


    /**
     * This method gets called when play button is pressed. Reset the board, set the player,
     * set current game, reset the board
     */
    public void initialize() {
        colorsRefresh();
        setOptions(false);

        if (this.mainCtrl.getPlayer() != null) {
            this.player = mainCtrl.getPlayer();
            currentGame = new GameInstance(this.player.getGameInstanceId(), 0);

            currentGame.generateQuestions(server.getActivitiesRandomly());

            gameQuestions.addAll(currentGame.getQuestions());
            progressBar.setProgress(-0.05);
            score.setText("Your score: 0");
            infoRefresh();
            temporaryCounter = 1;
            loadNextQuestion();
        }
        //TODO: generate the questions from database

    }

    /**
     * This method gets called before every round. Load next question, update the board.
     */
    public void loadNextQuestion() {
        //TODO: add support for different question types
        //TODO: when we get the activity bank, we will replace the hardcoded currentQuestion
        //this.currentQuestion = gameQuestions.poll();

        colorsRefresh();
        infoRefresh();
        setOptions(false);

        currentQuestion = currentGame.getRandomQuestion();

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                questionTitle.setText(currentQuestion.getTitle());
                option1Button.setText(((QuestionMoreExpensive)currentQuestion).getActivities()[0].getTitle());
                option2Button.setText(((QuestionMoreExpensive)currentQuestion).getActivities()[1].getTitle());
                option3Button.setText(((QuestionMoreExpensive)currentQuestion).getActivities()[2].getTitle());
                progressBar.setProgress(progressBar.getProgress() + 0.05);

                questionCount.setText("Question " + temporaryCounter + "/20");
                if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[0].getConsumption_in_wh())
                    correct_answer = option1Button;
                if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[1].getConsumption_in_wh())
                    correct_answer = option2Button;
                if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[2].getConsumption_in_wh())
                    correct_answer = option3Button;
            }
        });

        //TODO: set the images, reset/start the timer, add timer logic, implement power-ups
    }

    /**
     * This method is called when in a multiple choice question, user selects option 1
     */
    public void option1Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[0].getConsumption_in_wh()) {
            correctAnswer();
        }else {
            wrongAnswer();
        }
    }

    /**
     * This method is called when in a multiple choice question, user selects option 2
     */
    public void option2Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[1].getConsumption_in_wh()) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * This method is called when in a multiple choice question, user selects option 3
     */
    public void option3Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[2].getConsumption_in_wh()) {
            correctAnswer();
        }else {
            wrongAnswer();
        }
    }

    /**
     * User's answer was correct. Show that the answer was correct, update the score, start next round.
     */
    public void correctAnswer() {
        player.addScore(100);
        score.setText("Your score: " + player.getScore());
        points.setText("+100 points"); // In the future calculate the # of points, DON'T hardcode
        answer.setText("Correct answer");
        setEmoji(emoji, true);

        setColors();
        setOptions(true);

        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (temporaryCounter >= 20) {
            gameOver(2000);
        }

    }

    /**
     * User's answer was incorrect. Show that the answer was incorrect, start next round.
     */
    public void wrongAnswer() {
        points.setText("+0 points"); // In the future calculate the # of points, DON'T hardcode
        answer.setText("Wrong answer");
        setEmoji(emoji, false);

        setColors();
        setOptions(true);


        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (temporaryCounter >= 20) {
            gameOver(2000);
        }
    }

    /**
     * Check if the game is over.
     * Note that this method compares temporaryCounter to 20, and increments its value AFTER the comparison
     */
    public boolean isGameOver() {
        //return this.gameQuestions.isEmpty();
        return 20 == temporaryCounter++;
    }

    /**
     * Restarts the buttons to their original state -> all get the white background color
     */
    public void colorsRefresh() {
        option1Button.setStyle("-fx-background-color: #91e4fb; ");
        option2Button.setStyle("-fx-background-color: #91e4fb; ");
        option3Button.setStyle("-fx-background-color: #91e4fb; ");
    }

    /**
     * Makes the background of  the correct button GREEN and the background of the wrong buttons RED
     *
     *
     */
    public void setColors() {
        correct_answer.setStyle("-fx-background-color: green; ");
        if(option1Button!=correct_answer) option1Button.setStyle("-fx-background-color: red; ");
        if(option2Button!=correct_answer) option2Button.setStyle("-fx-background-color: red; ");
        if(option3Button!=correct_answer) option3Button.setStyle("-fx-background-color: red; ");
    }

    /**
     * Sets buttons as functional / disabled, depending on the parameter
     *
     * @param value - boolean value that disables our 3 option buttons if it is 'true', or makes them functional otherwise
     */
    public void setOptions(boolean value) {
        option1Button.setDisable(value);
        option2Button.setDisable(value);
        option3Button.setDisable(value);
    }

    /**
     * Sets the 'points' and 'answer' text fields to being empty strings.
     */
    public void infoRefresh() {
        points.setText("");
        answer.setText("");
        emoji.setVisible(false);
    }

    public void setEmoji(AnchorPane emoji, boolean correct) {
        emoji.setVisible(true);
        File file = null;
        if (correct)
            file = new File(correctEmojiPath);
        else
            file = new File(wrongEmojiPath);
        URI uri = file.toURI();
        emoji.setStyle("-fx-background-image: url(" + uri.toString() + ");");
    }


    /**
     * Freezes the scene for 'timer' miliseconds ('run' method of thread, the first one) and after this interval of time runs the
     * code inside the 'run'  method of Platform.runLater (the second one), by showing the user the gameOver screen
     *
     * @param timer - an integer value representing the number of miliseconds after which the thread executes
     */
    public void gameOver(int timer) {
        Thread thread = new Thread(new Runnable() {

            public void run() {

                try {
                    Thread.sleep(timer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    public void run() {
                        mainCtrl.showSinglePlayerGameOver();
                        progressBar.setProgress(1);
                    }
                });

            }
        });
        thread.start();
    }


}
