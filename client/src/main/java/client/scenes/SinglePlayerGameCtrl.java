package client.scenes;

import client.scenes.multiplayer.GameCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import commons.player.SimpleUser;
import jakarta.ws.rs.NotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

//Note that in the future, we can make this controller and its scene suitable for multiplayer games as well
public class SinglePlayerGameCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final GameCtrl gameCtrl;

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
    private Text questionTitle, timer, score, points, answer, option4, correct_guess, questionCount;

    @FXML
    private AnchorPane timerImage, emoji;

    @FXML
    private Button option1Button, option2Button, option3Button, correct_answer, submit_guess;

    @FXML
    private TextField player_answer;

    @FXML
    private RadioButton answer1, answer2, answer3;

    @FXML
    private ImageView image1, image2, image3, image4;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Pane confirmationExit;

    private static boolean gameIsOver;


    @Inject
    public SinglePlayerGameCtrl(ServerUtils server, MainCtrl mainCtrl, GameCtrl gameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.gameCtrl = gameCtrl;
    }


    /**
     * This method gets called when play button is pressed. Reset the board, set the player,
     * set current game, reset the board and  generates 20 questions in a 'smart' way.
     */
    public void initialize() {
        if (gameCtrl.getPlayer() != null) {
            this.player = gameCtrl.getPlayer();
            disablePopUp();
            currentGame = new GameInstance(this.player.getGameInstanceId(), 0);
            try {
                currentGame.generateQuestions(server.getActivitiesRandomly());
            } catch (NotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No activities found on server! Returning to lobby");
                alert.show();
                leaveGame();
                return;
            }
            setTimerImage(timerImage);
            progressBar.setProgress(-0.05);
            score.setText("Your score: 0");
            roundCounter = 1;
            gameIsOver = false;
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

        Platform.runLater(() -> {
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
        });

        //TODO: implement power-ups
    }

    /**
     * Sets the scene for the QuestionMoreExpensive
     * Computes which activity is the correct answer (correct_answer Button)
     * The titles of the activities are on option1Button, option2Button, option3Button
     */
    public void setScene1() {

        correct_guess.setVisible(false);
        player_answer.setVisible(false);
        submit_guess.setVisible(false);

        enableOptionsQuestionMoreExpensive();
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
     * Additional method that enables the options for QuestionMoreExpensive type of question
     */
    private void enableOptionsQuestionMoreExpensive() {
        option1Button.setVisible(true);
        option2Button.setVisible(true);
        option3Button.setVisible(true);
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
    }

    /**
     * Sets the scene for the QuestionHowMuch
     * option4 describes the title of the question
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
     * option4 describes the title of the question
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

        enableOptionsQuestionWhichOne();

        progressBar.setProgress(progressBar.getProgress() + 0.05);
        questionCount.setText("Question " + roundCounter + "/20");

        randomlyChooseCorrectAnswerButton();
    }

    /**
     * Randomly choose which one of the three RadioButtons(answer1, answer2, answer3) will hold the correct answer
     * The other 2 wrong answers are somewhat randomly generated
     * * TODO: Work on a 'smarter' randoml generation of wrong answers
     */
    private void randomlyChooseCorrectAnswerButton() {
        Random random = new Random();
        int random_correct_answer = random.nextInt(3 - 1 + 1) + 1;
        long consumption_correct_answer = ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh();

        long other_answer1 = Math.abs((60 * consumption_correct_answer) / 100); // -40%
        long other_answer2 = Math.abs((130 * consumption_correct_answer) / 100); // +30%
        long other_answer3 = Math.abs((150 * consumption_correct_answer) / 100); // +50%

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
     * Additional method that enables the options for QuestionWhichOne type of question
     */
    private void enableOptionsQuestionWhichOne() {
        answer1.setVisible(true);
        answer2.setVisible(true);
        answer3.setVisible(true);
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
    }

    /**
     * This method is called when user selects option 1 in a QuestionMoreExpensive
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
     * This method is called when user selects option 2 in a QuestionMoreExpensive
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
     * This method is called when user selects option 3 in a QuestionMoreExpensive
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
     * For QuestionMoreExpensive
     * This method is called when User's answer is correct.
     * It shows that the answer was correct, updates the score and displays the changes (see additional method below)
     * and starts the next round.
     */
    public void correctAnswer() {
        displayCorrectAnswerUpdates();

        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver())
                loadNextQuestion();
        });


        if (roundCounter >= 20) {
            gameOver(2000);
        }

    }

    /**
     * Additional method that updates user's score, displays the changes and that the answer was correct (along with a happy emoji)
     */
    private void displayCorrectAnswerUpdates() {
        answered = true;
        int numberOfPoints = calculatePoints(timeLeft);
        player.addScore(numberOfPoints);
        server.updatePlayer(player);
        score.setText("Your score: " + player.getScore());
        points.setText("+" + numberOfPoints + "points");
        answer.setText("Correct answer");
        setEmoji(emoji, true);
        setColors();
        setOptions(true);
    }

    /**
     * For the QuestionMoreExpensive
     * This method is called when User's answer is incorrect
     * It shows that the answer was incorrect, displays that the answer was incorrect (see additional method below)
     * and starts the next round
     */
    public void wrongAnswer() {
        displayWrongAnswerUpdates();

        CompletableFuture.delayedExecutor(1, SECONDS).execute(() -> {
            if (!isGameOver()) loadNextQuestion();
        });

        if (roundCounter >= 20) gameOver(2000);
    }

    /**
     * Additional method which displays that the answer was incorrect (along with a crying emoji)
     */
    private void displayWrongAnswerUpdates() {
        answered = true;
        points.setText("+0 points");
        answer.setText("Wrong answer");
        setEmoji(emoji, false);
        setColors();
        setOptions(true);
    }

    /**
     * If the image is locally saved on your machine, the method sets the images for every type of question
     * Otherwise prints "Image not found"
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
                image1.setImage(new Image(server.getImage(((QuestionMoreExpensive) currentQuestion).getActivities()[0])));
                image2.setImage(new Image(server.getImage(((QuestionMoreExpensive) currentQuestion).getActivities()[1])));
                image3.setImage(new Image(server.getImage(((QuestionMoreExpensive) currentQuestion).getActivities()[2])));
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
                image4.setImage(new Image(server.getImage(((QuestionWhichOne) currentQuestion).getActivity())));
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
                image4.setImage(new Image(server.getImage(((QuestionHowMuch) currentQuestion)
                        .getActivity())));
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
     * Restarts option1Button, option2Button and option3Button to their original state.
     * 'original state' means white background.
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
     * Sets buttons as eanbled / disabled, depending on the value of parameter.
     *
     * @param value boolean value that disables our buttons if 'true', or makes them functional otherwise
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
     * Sets the top-left information of this scene to the original state (not visible).
     */
    public void infoRefresh() {
        points.setText("");
        answer.setText("");
        emoji.setVisible(false);
    }

    /**
     * Sets the 'timerImage' AncorPane's image
     *
     * @param timerImage AnchorPane object that is meant to display a timer image.
     */
    public void setTimerImage(AnchorPane timerImage) {
        File file = new File(timerPath);
        URI uri = file.toURI();
        timerImage.setStyle("-fx-background-image: url(" + uri + ");");
    }

    /**
     * Sets the 'emoji' AnchorPane's image, depending on the value of parameter.
     *
     * @param emoji   AnchorPane object that is meant to display an emoji
     * @param correct boolean value
     *                if 'true': displays a happy emoji image (if the answer was correct)
     *                or a crying emoji image (otherwise)
     */
    public void setEmoji(AnchorPane emoji, boolean correct) {
        emoji.setVisible(true);
        File file;
        if (correct)
            file = new File(correctEmojiPath);
        else
            file = new File(wrongEmojiPath);
        URI uri = file.toURI();
        emoji.setStyle("-fx-background-image: url(" + uri + ");");
    }

    /**
     * For the QuestionHowMuch
     * Checks whether the input guess was correct.
     * Awards the player a number of points depending on how close he/she was to the correct answer (so partial
     * points are given to the player).
     * If the number the user inputted is not a valid long number, the user will be shown "Invalid number. Try again"
     * message above the textLabel where he/she inputted his/her name, and he will be asked for another input.
     */
    public void isGuessCorrect() {
        CharSequence input = player_answer.getCharacters();
        try {
            long number = Long.parseLong(input.toString());
            long correct_number = ((QuestionHowMuch) currentQuestion).getActivity().getConsumption_in_wh();
            awardPointsQuestionHowMuch(number, correct_number);

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
     * Additional method awards 100,75,50,25 or 0 points to a player, depending on how close he/she was to
     * the correct answer, on behalf of our chosen strategy for this type of question.
     *
     * @param number         long value that represents the number inputted by the player.
     * @param correct_number long value that represents the correct answer to our QuestionHowMuch type of question
     */
    public void awardPointsQuestionHowMuch(long number, long correct_number) {
        if (number == correct_number) {
            player.addScore(100);
            server.updatePlayer(player);
            score.setText("Your score: " + player.getScore());
            points.setText("+100 points");
            answer.setText("Correct answer");
            setEmoji(emoji, true);
        } else {
            if (number <= correct_number + (25 * correct_number) / 100 && number >= correct_number - (25 * correct_number) / 100) {
                player.addScore(75);
                server.updatePlayer(player);
                score.setText("Your score: " + player.getScore());
                points.setText("+75 points");
                answer.setText("Almost the correct answer");
                setEmoji(emoji, true);
            } else {
                if (number <= correct_number + (50 * correct_number) / 100 && number >= correct_number - (50 * correct_number) / 100) {
                    player.addScore(50);
                    server.updatePlayer(player);
                    score.setText("Your score: " + player.getScore());
                    points.setText("+50 points");
                    answer.setText("Not quite the correct answer");
                    setEmoji(emoji, true);
                } else {
                    if (number <= correct_number + (75 * correct_number) / 100 && number >= correct_number - (75 * correct_number) / 100) {
                        player.addScore(25);
                        server.updatePlayer(player);
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
    }

    /**
     * This method is called when a Player runs out of time and didn't make any guess
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
     * This method is called when user selects answer1 in a QuestionWhichOne type of question
     */
    public void answer1Selected() {
        long response = Long.parseLong(answer1.getText());
        isSelectionCorrect(answer1, response);
    }

    /**
     * This method is called when user selects answer2 in a QuestionWhichOne type of question
     */
    public void answer2Selected() {
        long response = Long.parseLong(answer2.getText());
        isSelectionCorrect(answer2, response);
    }

    /**
     * This method is called when user selects answer3 in a QuestionWhichOne type of question
     */
    public void answer3Selected() {
        long response = Long.parseLong(answer3.getText());
        isSelectionCorrect(answer3, response);
    }

    /**
     * For QuestionWhichOne
     * Checks whether the selected answer was correct and awards the player partial points, depending on how fast
     * he/she answered to this specific question, on behalf of our chosen strategy for this type of question.
     *
     * @param player_answer the RadioButton selected by the player
     * @param response      the correct answer
     */
    public void isSelectionCorrect(RadioButton player_answer, long response) {

        if (response == ((QuestionWhichOne) currentQuestion).getActivity().getConsumption_in_wh()) {
            int numberOfPoints = calculatePoints(timeLeft);
            player.addScore(numberOfPoints);
            server.updatePlayer(player);
            score.setText("Your score: " + player.getScore());
            points.setText("+" + numberOfPoints + "points");
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

    /**
     * This method starts the countdown and update the timer every second.
     * When the player runs out of time, it checks what type of question was asked and calls the appropriate method.
     * If the question was answered, the Thread is stopped.
     * If the player did not already answer and there is still time,
     * the countdown is decremented and its current value is displayed to the user
     *
     * @param time integer value that shows the player how many seconds are left to provide an answer.
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
                } else if (currentQ != currentQuestion || answered || !server.containsPlayer(player)) {
                    scheduler.shutdown();
                } else {
                    setTimeLeft(countdown);
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
     * @param timer - an integer value representing the number of miliseconds after which the thread get executed.
     */
    public void gameOver(int timer) {
        if (gameIsOver == false) {
            server.addPlayerToLeaderboard(player);
            server.disconnect(player);
        }
        gameIsOver = true;
        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                mainCtrl.showSinglePlayerGameOver();
                progressBar.setProgress(1);
            });

        });
        thread.start();
    }

    /**
     * For multiple choice types of questions (QuestionWhichOne and QuestionMoreExpensive).
     * Additional method that calculates how many points should a player be awarded if he answered to a specific
     * question in 'timeLeft' seconds. The formula was chosen for the 20 seconds type of question, so answering in i.e:
     * 15 seconds gives 75 points to the player, 12 seconds -> 60 points, etc.
     *
     * @param timeLeft integer value representing how many seconds are left to answer a specific question
     */
    public int calculatePoints(int timeLeft) {
        return (timeLeft * 10) / 2;
    }

    /**
     * Sets how many seconds are left for the user to answer a specific question
     *
     * @param timeLeft integer value that represents the number of seconds remained to answer a question
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * This method is triggered when the player confirms that he wants to leave the game by pressing 'YES' button.
     * Works the same as 'back' method from previous version.
     */
    public void leaveGame() {
        server.disconnect(player);
        mainCtrl.showSplash();
    }

    /**
     * Makes the confirmation pop-up invisible
     */
    public void disablePopUp() {
        confirmationExit.setOpacity(0);
        confirmationExit.setDisable(true);
    }

    /**
     * Makes the confirmation pop-up visible
     * This method is triggered when the player presses the 'LEAVE BUTTON' game.
     * TODO: trigger the same method when clicking on 'x' of the window
     */
    public void enablePopUp() {
        confirmationExit.setOpacity(1);
        confirmationExit.setDisable(false);
        confirmationExit.setStyle("-fx-background-color: #91e4fb; ");
    }

    public static boolean getGameIsOver() {
        return gameIsOver;
    }

}
