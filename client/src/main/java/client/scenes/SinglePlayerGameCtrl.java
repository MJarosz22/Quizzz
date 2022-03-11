package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import commons.player.SimpleUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
    private TextField player_answer;

    @FXML
    private Text correct_guess;

    @FXML
    private Button submit_guess;

    @FXML
    private RadioButton answer1;

    @FXML
    private RadioButton answer2;

    @FXML
    private RadioButton answer3;

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
                if(currentQuestion instanceof QuestionMoreExpensive) {
                    correct_guess.setVisible(false);
                    player_answer.setVisible(false);
                    submit_guess.setVisible(false);
                    option1Button.setVisible(true);
                    option2Button.setVisible(true);
                    option3Button.setVisible(true);
                    answer1.setVisible(false);
                    answer2.setVisible(false);
                    answer3.setVisible(false);
                    option1Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[0].getTitle());
                    option2Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[1].getTitle());
                    option3Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[2].getTitle());
                    progressBar.setProgress(progressBar.getProgress() + 0.05);

                    questionCount.setText("Question " + temporaryCounter + "/20");
                    if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[0].getConsumption_in_wh())
                        correct_answer = option1Button;
                    if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[1].getConsumption_in_wh())
                        correct_answer = option2Button;
                    if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion).getActivities()[2].getConsumption_in_wh())
                        correct_answer = option3Button;

                }
                if (currentQuestion instanceof QuestionHowMuch) {
                    player_answer.clear();
                    option1Button.setText(((QuestionHowMuch) currentQuestion).getActivity().getTitle());
                    option1Button.disabledProperty();
                    option2Button.setVisible(false);
                    option3Button.setVisible(false);
                    player_answer.setVisible(true);
                    submit_guess.setVisible(true);
                    correct_guess.setVisible(false);
                    answer1.setVisible(false);
                    answer2.setVisible(false);
                    answer3.setVisible(false);
                    progressBar.setProgress(progressBar.getProgress() + 0.05);
                    questionCount.setText("Question " + temporaryCounter + "/20");
                }
                if (currentQuestion instanceof QuestionWhichOne){
                    answer1.setSelected(false);
                    answer2.setSelected(false);
                    answer3.setSelected(false);
                    answer1.setStyle("-fx-background-color: #91e4fb; ");
                    answer2.setStyle("-fx-background-color: #91e4fb; ");
                    answer3.setStyle("-fx-background-color: #91e4fb; ");
                    option1Button.setText(((QuestionWhichOne) currentQuestion).getActivity().getTitle());
                    option1Button.disabledProperty();
                    option2Button.setVisible(false);
                    option3Button.setVisible(false);
                    player_answer.setVisible(false);
                    submit_guess.setVisible(false);
                    correct_guess.setVisible(false);
                    answer1.setVisible(true);
                    answer2.setVisible(true);
                    answer3.setVisible(true);
                    progressBar.setProgress(progressBar.getProgress() + 0.05);
                    questionCount.setText("Question " + temporaryCounter + "/20");
                    Random random = new Random();
                    int random_correct_answer = random.nextInt(3 - 1 + 1) + 1;
                    long other_answer1 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() - 500);
                    long other_answer2 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() + 700);
                    long other_answer3 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() - 200);
                    if(random_correct_answer==1)answer1.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
                    else answer1.setText(String.valueOf(other_answer1));
                    if(random_correct_answer==2)answer2.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
                    else answer2.setText(String.valueOf(other_answer2));
                    if(random_correct_answer==3)answer3.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
                    else answer3.setText(String.valueOf(other_answer3));
                }
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

    public void isGuessCorrect() {
        CharSequence input = player_answer.getCharacters();
        long number = Long.parseLong(input.toString());
        if( number == ((QuestionHowMuch) currentQuestion).getActivity().getConsumption_in_wh()) {
            player.addScore(100);
            score.setText("Your score: " + player.getScore());
            points.setText("+100 points");
            answer.setText("Correct answer");
            setEmoji(emoji, true);
        }
        else{
            points.setText("+0 points");
            answer.setText("Wrong answer");
            setEmoji(emoji, false);
        }
        correct_guess.setVisible(true);
        correct_guess.setText("The correct answer is: "+ ((QuestionHowMuch) currentQuestion).getActivity().getConsumption_in_wh());
        setOptions(true);

        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (temporaryCounter >= 20) {
            gameOver(2000);
        }
    }


    public void answer1Selected(){
        long response = Long.parseLong(answer1.getText());
        isSelectionCorrect(answer1,response);
    }

    public void answer2Selected(){
        long response = Long.parseLong(answer2.getText());
        isSelectionCorrect(answer2,response);
    }

    public void answer3Selected(){
        long response = Long.parseLong(answer3.getText());
        isSelectionCorrect(answer3,response);
    }

    public void isSelectionCorrect(RadioButton player_answer, long response) {

        if (response == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh()){
            player.addScore(100);
            score.setText("Your score: " + player.getScore());
            points.setText("+100 points");
            answer.setText("Correct answer");
            setEmoji(emoji, true);
            player_answer.setStyle("-fx-background-color: green; ");
            if(!answer1.equals(player_answer)) answer1.setStyle("-fx-background-color: red; ");
            if(!answer2.equals(player_answer)) answer2.setStyle("-fx-background-color: red; ");
            if(!answer3.equals(player_answer)) answer3.setStyle("-fx-background-color: red; ");
        }
        else{
            points.setText("+0 points");
            answer.setText("Wrong answer");
            setEmoji(emoji, false);
            if(Long.parseLong(answer1.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer1.setStyle("-fx-background-color: green; ");
            else answer1.setStyle("-fx-background-color: red; ");
            if(Long.parseLong(answer2.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer2.setStyle("-fx-background-color: green; ");
            else answer2.setStyle("-fx-background-color: red; ");
            if(Long.parseLong(answer3.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer3.setStyle("-fx-background-color: green; ");
            else answer3.setStyle("-fx-background-color: red; ");
        }

        setOptions(true);

        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (temporaryCounter >= 20) {
            gameOver(2000);
        }
    }
}
