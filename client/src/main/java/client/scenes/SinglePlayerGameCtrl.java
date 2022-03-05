package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.*;

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
        if (this.mainCtrl.getPlayer() != null) {
            this.player = mainCtrl.getPlayer();
            currentGame = new GameInstance(this.player.getGameInstanceId(), 0);
        }
        //TODO: generate the questions from database
        //currentGame.generateQuestions(*list of activities*);
        //gameQuestions.addAll(currentGame.getQuestions());
        progressBar.setProgress(-0.05);
        score.setText("Your score: 0");
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
            //set the color to green
            correctAnswer();
        } else wrongAnswer();
    }

    public void option2Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[1])) {
            //set the color to green
            correctAnswer();
        } else wrongAnswer();
    }

    public void option3Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[2])) {
            //set the color to green
            correctAnswer();
        } else wrongAnswer();
    }

    /**
     * User's answer was correct. Show that the answer was correct, update the score, start next round.
     */
    public void correctAnswer() {
        player.addScore(100);
        score.setText("Your score: " + player.getScore());
        //TODO:
        //set the color of the button to green
        //make a prompt "correct answer"

        if (!isGameOver())
            loadNextQuestion();
        else {
            mainCtrl.showSinglePlayerGameOver();
            progressBar.setProgress(1);
        }
    }

    /**
     * User's answer was correct. Show that the answer was incorrect, start next round.
     */
    public void wrongAnswer() {
        //TODO:
        //set the color of the correct button to green
        //make a prompt "wrong answer"
        if (!isGameOver())
            loadNextQuestion();
        else {
            mainCtrl.showSinglePlayerGameOver();
            progressBar.setProgress(1);
        }
    }

    /**
     * Check if the game is over
     */
    public boolean isGameOver() {
        //return this.gameQuestions.isEmpty();
        return 20 == temporaryCounter++;
    }
}
