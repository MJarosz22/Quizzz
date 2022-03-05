package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class SinglePlayerGameCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    //TODO: For now there is only GameInstance, we are not making use of the player stored on the server
    //private SimpleUser player;

    private GameInstance currentGame;
    private Queue<Question> gameQuestions = new LinkedList<>();
    private Question currentQuestion;


    int temporaryCounter = 1;
    int temporaryScore = 0;

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
        //this.player = player; (or something like this)
    }

    public void back() {
        mainCtrl.showSplash();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentGame = new GameInstance();
        //currentGame.generateQuestions(*list of activities*);
        //gameQuestions.addAll(currentGame.getQuestions());
        progressBar.setProgress(-0.05);
        score.setText("Your score: 0");
        loadNextQuestion();
    }

    public void loadNextQuestion() {
        //TODO: when we get the activity bank, we will replace the hardcoded currentQuestion
        //this.currentQuestion = gameQuestions.poll();
        //TODO: add support for different question types
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

    public void option1Selected() {
        if (((MultipleChoiceQuestion) currentQuestion).getAnswer().equals(currentQuestion.getActivities()[0])) {
            //set the color to green
            correctAnswer();
        }
        //set the color to red
        else wrongAnswer();
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
        }
        //set the color to red
        else wrongAnswer();
    }

    public void correctAnswer() {
        //TODO:
        //player.addScore(*the added score*)
        //set the color of the button to green
        //score.setText("Your score: " + player.score);
        //make a prompt "correct answer"
        temporaryScore += 100;
        score.setText("Your score: " + temporaryScore);

        if (!isGameOver())
            loadNextQuestion();
        else {
            mainCtrl.showSinglePlayerGameOver();
            progressBar.setProgress(1);
        }
    }

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

    public boolean isGameOver() {
        //return this.gameQuestions.isEmpty();
        return 20 == temporaryCounter++;
    }
}
