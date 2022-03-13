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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

//Note that in the future, we can make this controller and its scene suitable for multiplayer games as well
public class SinglePlayerGameCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final String correctEmojiPath = "client/src/main/resources/images/correct-answer.png";
    private final String wrongEmojiPath = "client/src/main/resources/images/wrong-answer.png";
    private final String timerPath = "client/src/main/resources/images/timer.png";

    private SimpleUser player;
    private GameInstance currentGame;
    private Question currentQuestion;

    private boolean answered;
    private int timeLeft;
    private int roundCounter;

    @FXML
    private Text questionTitle;

    @FXML
    private Text timer;

    @FXML
    private AnchorPane timerImage;

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
    private Text option4;

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
    private ImageView image4;

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
        setTimerImage(timerImage);

        if (this.mainCtrl.getPlayer() != null) {
            this.player = mainCtrl.getPlayer();
            currentGame = new GameInstance(this.player.getGameInstanceId(), 0);

            currentGame.generateQuestions(server.getActivitiesRandomly());

            progressBar.setProgress(-0.05);
            score.setText("Your score: 0");
            infoRefresh();
            roundCounter = 1;
            loadNextQuestion();
        }

    }

    /**
     * This method gets called before every round. Load next question, update the board.
     */
    public void loadNextQuestion() {
        colorsRefresh();
        infoRefresh();
        setOptions(false);

        currentQuestion = currentGame.getRandomQuestion();
        setImages();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timeLeft = 20;

                timer.setText(String.valueOf(timeLeft));
                questionTitle.setText(currentQuestion.getTitle());
                if (currentQuestion instanceof QuestionMoreExpensive) {
                    setScene1();
                }

                if (currentQuestion instanceof QuestionHowMuch) {
                    setScene2();
                }
                if (currentQuestion instanceof QuestionWhichOne) {
                    setScene3();
                }
                answered = false;
                startTimer(20);
            }
        });

        //TODO: implement power-ups
    }

    /**
     * Sets the scene for the QuestionMoreExpensive
     * Knows which activity is the correct answer (correct_answer)
     * The titles of the activities are on option1Button, option2Button, option3Button
     */

    public void setScene1() {

        correct_guess.setVisible(false);
        player_answer.setVisible(false);
        submit_guess.setVisible(false);

        option1Button.setVisible(true);
        option2Button.setVisible(true);
        option3Button.setVisible(true);
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
        option4.setVisible(false);

        answer1.setVisible(false);
        answer2.setVisible(false);
        answer3.setVisible(false);

        option1Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[0].getTitle());
        option2Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[1].getTitle());
        option3Button.setText(((QuestionMoreExpensive) currentQuestion).getActivities()[2].getTitle());

        progressBar.setProgress(progressBar.getProgress() + 0.05);
        questionCount.setText("Question " + roundCounter + "/20");

        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[0].getConsumption_in_wh())
            correct_answer = option1Button;

        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[1].getConsumption_in_wh())
            correct_answer = option2Button;

        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[2].getConsumption_in_wh())
            correct_answer = option3Button;
    }

    /**
     * Sets the scene for the QuestionHowMuch
     * Sets the title of the activity on option4
     */
    public void setScene2() {
        player_answer.clear();
        setOptions(true);
        option4.setText(((QuestionHowMuch) currentQuestion).getActivity().getTitle());

        option1Button.setVisible(false);
        option2Button.setVisible(false);
        option3Button.setVisible(false);
        option4.setVisible(true);

        player_answer.setVisible(true);
        submit_guess.setVisible(true);
        submit_guess.setDisable(false);
        correct_guess.setVisible(false);

        answer1.setVisible(false);
        answer2.setVisible(false);
        answer3.setVisible(false);

        progressBar.setProgress(progressBar.getProgress() + 0.05);
        questionCount.setText("Question " + roundCounter + "/20");
    }

    /**
     * Sets the scene for the QuestionWhichOne
     * Sets the title for the activity on option4
     * Randomly choose which one of the three RadioButtons(answer1, answer2, answer3) has the correct answer
     * The other 2 wrong answers are somewhat randomly generated
     */
    public void setScene3() {
        answer1.setSelected(false);
        answer2.setSelected(false);
        answer3.setSelected(false);

        answer1.setStyle("-fx-background-color: #91e4fb; ");
        answer2.setStyle("-fx-background-color: #91e4fb; ");
        answer3.setStyle("-fx-background-color: #91e4fb; ");


        option4.setText(((QuestionWhichOne) currentQuestion).getActivity().getTitle());

        option1Button.setVisible(false);
        option2Button.setVisible(false);
        option3Button.setVisible(false);
        option4.setVisible(true);

        setOptions(true);
        player_answer.setVisible(false);
        submit_guess.setVisible(false);
        correct_guess.setVisible(false);

        answer1.setVisible(true);
        answer2.setVisible(true);
        answer3.setVisible(true);
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);

        progressBar.setProgress(progressBar.getProgress() + 0.05);
        questionCount.setText("Question " + roundCounter + "/20");

        Random random = new Random();
        int random_correct_answer = random.nextInt(3 - 1 + 1) + 1;

        long other_answer1 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() - 500);
        long other_answer2 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() + 700);
        long other_answer3 = Math.abs(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh() - 200);

        if (random_correct_answer == 1)
            answer1.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
        else answer1.setText(String.valueOf(other_answer1));

        if (random_correct_answer == 2)
            answer2.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
        else answer2.setText(String.valueOf(other_answer2));

        if (random_correct_answer == 3)
            answer3.setText(((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh().toString());
        else answer3.setText(String.valueOf(other_answer3));
    }

    /**
     * This method is called when in a multiple choice question (QuestionMoreExpensive), user selects option 1
     */
    public void option1Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[0].getConsumption_in_wh()) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * This method is called when in a multiple choice question (QuestionMoreExpensive), user selects option 2
     */
    public void option2Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[1].getConsumption_in_wh()) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * This method is called when in a multiple choice question (QuestionMoreExpensive), user selects option 3
     */
    public void option3Selected() {
        if (((QuestionMoreExpensive) currentQuestion).getAnswer() == ((QuestionMoreExpensive) currentQuestion)
                .getActivities()[2].getConsumption_in_wh()) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    /**
     * For the QuestionMoreExpensive
     * User's answer was correct. Show that the answer was correct, update the score, start next round.
     */
    public void correctAnswer() {
        answered = true;
        player.addScore(100);
        score.setText("Your score: " + player.getScore());
        points.setText("+100 points"); // In the future calculate the # of points, DON'T hardcode
        answer.setText("Correct answer");
        setEmoji(emoji, true);

        setColors();
        setOptions(true);

        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (roundCounter >= 20) {
            gameOver(2000);
        }

    }

    /**
     * For the QuestionMoreExpensive
     * User's answer was incorrect. Show that the answer was incorrect, start next round.
     */
    public void wrongAnswer() {
        answered = true;
        points.setText("+0 points"); // In the future calculate the # of points, DON'T hardcode
        answer.setText("Wrong answer");
        setEmoji(emoji, false);

        setColors();
        setOptions(true);


        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (roundCounter >= 20) {
            gameOver(2000);
        }
    }

    /**
     * Sets the images for every type of question
     */
    public void setImages() {
        String activitiesPath = new File("").getAbsolutePath();
        activitiesPath += "\\client\\src\\main\\resources\\images\\activities\\";
        if (currentQuestion instanceof QuestionMoreExpensive) {
            image1.setVisible(true);
            image2.setVisible(true);
            image3.setVisible(true);
            image4.setVisible(false);
            try {
                image1.setImage(new Image(new FileInputStream(activitiesPath + ((QuestionMoreExpensive) currentQuestion)
                        .getActivities()[0].getImage_path().replace("/", "\\"))));
                image2.setImage(new Image(new FileInputStream(activitiesPath + ((QuestionMoreExpensive) currentQuestion)
                        .getActivities()[1].getImage_path().replace("/", "\\"))));
                image3.setImage(new Image(new FileInputStream(activitiesPath + ((QuestionMoreExpensive) currentQuestion)
                        .getActivities()[2].getImage_path().replace("/", "\\"))));
            } catch (FileNotFoundException e) {
                System.out.println("Image not found!");
            }
        }

        if (currentQuestion instanceof QuestionWhichOne) {
            image1.setVisible(false);
            image2.setVisible(false);
            image3.setVisible(false);
            image4.setVisible(true);
            try {
                image4.setImage(new Image(new FileInputStream(activitiesPath + ((QuestionWhichOne) currentQuestion)
                        .getActivity().getImage_path().replace("/", "\\"))));
            } catch (FileNotFoundException e) {
                System.out.println("Image not found!");
            }
        }

        if (currentQuestion instanceof QuestionHowMuch) {
            image1.setVisible(false);
            image2.setVisible(false);
            image3.setVisible(false);
            image4.setVisible(true);
            try {
                image4.setImage(new Image(new FileInputStream(activitiesPath + ((QuestionHowMuch) currentQuestion)
                        .getActivity().getImage_path().replace("/", "\\"))));
            } catch (FileNotFoundException e) {
                System.out.println("Image not found!");
            }
        }
    }


    /**
     * Check if the game is over.
     * Note that this method compares temporaryCounter to 20, and increments its value AFTER the comparison
     */
    public boolean isGameOver() {
        return 20 == roundCounter++;
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
     * For the QuestionMoreExpensive
     * Makes the background of  the correct button GREEN and the background of the wrong buttons RED
     */
    public void setColors() {
        correct_answer.setStyle("-fx-background-color: green; ");
        if (option1Button != correct_answer) option1Button.setStyle("-fx-background-color: red; ");
        if (option2Button != correct_answer) option2Button.setStyle("-fx-background-color: red; ");
        if (option3Button != correct_answer) option3Button.setStyle("-fx-background-color: red; ");
    }

    /**
     * Sets buttons as functional / disabled, depending on the parameter
     *
     * @param value - boolean value that disables our 3 option buttons if it is 'true', or makes them functional otherwise
     */
    public void setOptions(boolean value) {
        answer1.setDisable(value);
        answer2.setDisable(value);
        answer3.setDisable(value);
        submit_guess.setDisable(value);
        option1Button.setDisable(value);
        option2Button.setDisable(value);
        option3Button.setDisable(value);
        option4.setDisable(value);
    }

    /**
     * Sets the 'points' and 'answer' text fields to being empty strings.
     */
    public void infoRefresh() {
        points.setText("");
        answer.setText("");
        emoji.setVisible(false);
    }

    /**
     * sets the 'timerImage' anchorpane's image
     *
     * @param timerImage
     */
    public void setTimerImage(AnchorPane timerImage) {
        File file = new File(timerPath);
        URI uri = file.toURI();
        timerImage.setStyle("-fx-background-image: url(" + uri.toString() + ");");
    }

    /**
     * sets the 'emoji' anchorpane's image, based boolean value
     *
     * @param emoji
     * @param correct
     */
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
     * For the QuestionHowMuch
     * Checks whether the input guess was correct
     * If the number the user inputted is not a valid long number, the user will be shown "Invalid number. Try again"
     * message above the textLabel where he/she inputted his/her name, and he will be asked for another input.
     */
    public void isGuessCorrect() {
        CharSequence input = player_answer.getCharacters();
        try {
            long number = Long.parseLong(input.toString());
            long correct_number = ((QuestionHowMuch) currentQuestion).getActivity().getConsumption_in_wh();
            if (number == correct_number) {
                player.addScore(100);
                score.setText("Your score: " + player.getScore());
                points.setText("+100 points");
                answer.setText("Correct answer");
                setEmoji(emoji, true);
            } else {
                if (number <= correct_number + (25 * correct_number) / 100 && number >= correct_number - (25 * correct_number) / 100) {
                    player.addScore(75);
                    score.setText("Your score: " + player.getScore());
                    points.setText("+75 points");
                    answer.setText("Almost the correct answer");
                    setEmoji(emoji, true);
                } else {
                    if (number <= correct_number + (50 * correct_number) / 100 && number >= correct_number - (50 * correct_number) / 100) {
                        player.addScore(50);
                        score.setText("Your score: " + player.getScore());
                        points.setText("+50 points");
                        answer.setText("Not quite the correct answer");
                        setEmoji(emoji, true);
                    } else {
                        if (number <= correct_number + (75 * correct_number) / 100 && number >= correct_number - (75 * correct_number) / 100) {
                            player.addScore(25);
                            score.setText("Your score: " + player.getScore());
                            points.setText("+25 points");
                            answer.setText("Pretty far from the correct answer");
                            setEmoji(emoji, true);
                        } else {
                            points.setText("+0 points");
                            answer.setText("Wrong answer");
                            setEmoji(emoji, false);
                        }
                    }
                }

            }
            correct_guess.setVisible(true);
            correct_guess.setText("The correct answer is: " + correct_number);
            setOptions(true);

            CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
                if (!isGameOver())
                    loadNextQuestion();
            });

            if (roundCounter >= 20) {
                gameOver(2000);
            }
        } catch (NumberFormatException e) {
            player_answer.clear();
            correct_guess.setVisible(true);
            correct_guess.setText("Invalid number. Try again.");
        }
    }

    /**
     * Player ran out of time and didn't make any guess
     */
    public void noGuess() {
        correct_guess.setVisible(true);
        correct_guess.setText("The correct answer is: " + ((QuestionHowMuch) currentQuestion).getActivity().getConsumption_in_wh());
        setOptions(true);

        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });

        if (roundCounter >= 20) {
            gameOver(2000);
        }
    }

    /**
     * For QuestionWhichOne if answer1 was selected
     */
    public void answer1Selected() {
        long response = Long.parseLong(answer1.getText());
        isSelectionCorrect(answer1, response);
    }

    /**
     * For QuestionWhichOne if answer2 was selected
     */
    public void answer2Selected() {
        long response = Long.parseLong(answer2.getText());
        isSelectionCorrect(answer2, response);
    }

    /**
     * For QuestionWhichOne if answer3 was selected
     */
    public void answer3Selected() {
        long response = Long.parseLong(answer3.getText());
        isSelectionCorrect(answer3, response);
    }

    /**
     * For QuestionWhichOne
     * Checks whether the selected answer was correct
     *
     * @param player_answer the selected RadioButton of the player
     * @param response      the consumption from the selected RadioButton
     */
    public void isSelectionCorrect(RadioButton player_answer, long response) {

        if (response == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh()) {
            player.addScore(100);
            score.setText("Your score: " + player.getScore());
            points.setText("+100 points");
            answer.setText("Correct answer");
            setEmoji(emoji, true);
            player_answer.setStyle("-fx-background-color: green; ");
            if (!answer1.equals(player_answer)) answer1.setStyle("-fx-background-color: red; ");
            if (!answer2.equals(player_answer)) answer2.setStyle("-fx-background-color: red; ");
            if (!answer3.equals(player_answer)) answer3.setStyle("-fx-background-color: red; ");
        } else {
            points.setText("+0 points");
            answer.setText("Wrong answer");
            setEmoji(emoji, false);
            if (Long.parseLong(answer1.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer1.setStyle("-fx-background-color: green; ");
            else answer1.setStyle("-fx-background-color: red; ");
            if (Long.parseLong(answer2.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer2.setStyle("-fx-background-color: green; ");
            else answer2.setStyle("-fx-background-color: red; ");
            if (Long.parseLong(answer3.getText()) == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh())
                answer3.setStyle("-fx-background-color: green; ");
            else answer3.setStyle("-fx-background-color: red; ");
        }

        setOptions(true);

        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (roundCounter >= 20) {
            gameOver(2000);
        }
    }

//I put startTimer and startCountdown in one method
    /**
     * Start the countdown. Update the timer every second.
     *
     * @param time time in miliseconds
     */
    public void startTimer(int time) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Question currentQ = currentQuestion;
        Runnable runnable = new Runnable() {
            int countdown = time;

            public void run() {
                if (countdown == 0) {
                    if (currentQ instanceof QuestionHowMuch)
                        noGuess();
                    else if (currentQ instanceof QuestionWhichOne)
                        isSelectionCorrect(null, 0);
                    else
                        wrongAnswer();
                    timer.setText(String.valueOf(countdown));
                    scheduler.shutdown();
                } else if (currentQ != currentQuestion || answered) {
                    scheduler.shutdown();
                } else {
                    timer.setText(String.valueOf(countdown--));
                }

            }
        };
        scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
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
