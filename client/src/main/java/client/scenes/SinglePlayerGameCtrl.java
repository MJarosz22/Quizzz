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
import javafx.scene.text.Text;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//Note that in the future, we can make this controller and its scene suitable for multiplayer games as well
public class SinglePlayerGameCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

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
    private Button option1Button;

    @FXML
    private Button option2Button;

    @FXML
    private Button option3Button;

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
        }
        //TODO: generate the questions from database
        //currentGame.generateQuestions(*list of activities*);
        //gameQuestions.addAll(currentGame.getQuestions());
        progressBar.setProgress(-0.05);
        score.setText("Your score: 0");
        pointsAndAnswerRefresh();
        temporaryCounter = 1;
        loadNextQuestion();
    }

    /**
     * This method gets called before every round. Load next question, update the board.
     */
    public void loadNextQuestion() {
        //TODO: add support for different question types
        //TODO: when we get the activity bank, we will replace the hardcoded currentQuestion
        //this.currentQuestion = gameQuestions.poll();

        colorsRefresh();
        pointsAndAnswerRefresh();
        setOptions(false);

        Activity temporaryActivity1 = new Activity("correctAnswer", 100, "source");
        Activity temporaryActivity2 = new Activity("wrongAnswer", 100, "source");
        Activity temporaryActivity3 = new Activity("wrongAnswer", 100, "source");
        Activity[] temporaryActivities = {temporaryActivity1, temporaryActivity2, temporaryActivity3};

        currentQuestion = new MultipleChoiceQuestion(temporaryActivities);

        questionTitle.setText("QuestionTitle");

        option1Button.setText(currentQuestion.getActivities()[0].title);
        option2Button.setText(currentQuestion.getActivities()[1].title);
        option3Button.setText(currentQuestion.getActivities()[2].title);

        progressBar.setProgress(progressBar.getProgress() + 0.05);

        questionCount.setText("Question " + temporaryCounter + "/20");
        //TODO: set the images, reset/start the timer, add timer logic, implement power-ups
    }

    /**
     * This method is called when in a multiple choice question, user selects option 1
     */
    public void option1Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[0])) {
            correctAnswer();
        } else wrongAnswer();
    }

    /**
     * This method is called when in a multiple choice question, user selects option 2
     */
    public void option2Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[1])) {
            correctAnswer();
        } else wrongAnswer();
    }

    /**
     * This method is called when in a multiple choice question, user selects option 3
     */
    public void option3Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[2])) {
            correctAnswer();
        } else wrongAnswer();
    }

    /**
     * User's answer was correct. Show that the answer was correct, update the score, start next round.
     */
    public void correctAnswer() {
        player.addScore(100);
        score.setText("Your score: " + player.getScore());
        points.setText("+100 points"); // In the future calculate the # of points, DON'T hardcode
        answer.setText("Correct answer  \uD83E\uDD29"); // Code of excited emoji, in the future find a better solution

        setColors(option1Button, option2Button, option3Button);
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
        answer.setText("Incorrect answer  \uD83D\uDE2D"); // Code of crying, in the future find a better solution

        setColors(option1Button, option2Button, option3Button);
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
        option1Button.setStyle("-fx-background-color: white; ");
        option2Button.setStyle("-fx-background-color: white; ");
        option3Button.setStyle("-fx-background-color: white; ");
    }

    /**
     * Makes the background of  the correct button GREEN and the background of the wrong buttons RED
     *
     * @param correct - Button object that represents the correct option for a MC question
     * @param wrong1  - Button object that represents one incorrect option for a MC question
     * @param wrong2  - Button object that represents another incorrect option for a MC question
     */
    public void setColors(Button correct, Button wrong1, Button wrong2) {
        correct.setStyle("-fx-background-color: green; ");
        wrong1.setStyle("-fx-background-color: red; ");
        wrong2.setStyle("-fx-background-color: red; ");
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
    public void pointsAndAnswerRefresh() {
        points.setText("");
        answer.setText("");
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
