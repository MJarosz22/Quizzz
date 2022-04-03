package server.api;

import commons.*;
import commons.player.SimpleUser;
import commons.powerups.AnswerPU;
import commons.powerups.PointsPU;
import commons.powerups.TimePU;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameInstanceServer extends GameInstance {

    String serverName;
    GameController gameController;
    SimpMessagingTemplate msgs;
    int questionNumber = -1;
    private static final int questionTime = 12000;
    private static final int postQuestionTime = 5000;
    private final List<ServerAnswer> answers;
    private long startingTime;
    Logger logger = LoggerFactory.getLogger(GameInstanceServer.class);
    private TimerTask questionTask;
    private final Timer questionTimer;


    private final Timer countdownTimer;

    /**
     * Creates new GameInstance for multiplayer purposes
     * @param id ID of GameInstance
     * @param type Type of GameInstance (SINGLEPLAYER OR MULTIPLAYER)
     * @param controller GameController (injected)
     * @param msgs SimpMessagingTemplate for websockets
     * @param serverName Name of Server
     */
    public GameInstanceServer(int id, int type, GameController controller, SimpMessagingTemplate msgs, String serverName) {
        super(id, type);
        this.gameController = controller;
        this.msgs = msgs;
        this.serverName = serverName;
        answers = new ArrayList<>();
        questionTimer = new Timer();
        countdownTimer = new Timer();
    }


    /**
     * QuestionInsteadOf uses activity 0,1,2,3
     * QuestionWhichOne uses activity 4
     * QuestionHowMuch uses activity 5
     * QuestionMoreExpensive uses activity 6,7,8
     * After that, the mod is 1 and QuestionInsteadOf uses activity 9 etc
     *
     * @param activities List of 60 activities
     */
    @Override
    public void generateQuestions(List<Activity> activities) {
        if (activities.size() != 60) throw new IllegalArgumentException();
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int remainder = i % 4;
            int mod = i / 4;
            if (questions.size() == 20) break;
            if (remainder == 3) questions.add(new QuestionMoreExpensive
                    (new Activity[]{
                            activities.get(9 * mod + 6),
                            activities.get(9 * mod + 7),
                            activities.get(9 * mod + 8)},
                            i + 1));
            else if (remainder == 2) questions.add(new QuestionHowMuch(activities.get(9 * mod + 5), i + 1));
            else if (remainder == 1) questions.add(new QuestionWhichOne(activities.get(9 * mod + 4), i + 1));
            else questions.add(new QuestionInsteadOf(activities.get(9 * mod),
                        new Activity[]{activities.get(9 * mod + 1),
                                activities.get(9 * mod + 2), activities.get(9 * mod + 3)}, i + 1));
        }
        setQuestions(questions);
    }

    /**
     * Starts countdown of this gameInstance, and starts the game after n seconds
     */
    public void startCountdown() {
        setState(GameState.STARTING);

        TimerTask countdownTask = new TimerTask() {
            int time = 6;

            @Override
            public void run() {
                time--;
                msgs.convertAndSend("/topic/" + getId() + "/time", time);
                if (time == 0) {
                    cancel();
                    startGame(gameController.activityController.getRandom60().getBody());
                }
            }
        };

        countdownTimer.scheduleAtFixedRate(countdownTask, 0, 1000);
    }

    /**
     * Starts the game (without countdown)
     * @param activities List of activities used for generating questions (needs 60 activities.)
     */
    @Async
    public void startGame(List<Activity> activities) {
        generateQuestions(activities);
        nextQuestion();
    }

    /**
     * Sends a new question to all clients connected to this gameInstance
     * @param questionNumber Number of question that needs to be sent.
     */
    private void sendQuestion(int questionNumber) {
        Question currentQuestion = getQuestions().get(questionNumber);
        logger.info("[GI " + getId() + "] Question " + questionNumber + " sent.");
        if (currentQuestion instanceof QuestionHowMuch) {
            msgs.convertAndSend("/topic/" + getId() + "/questionhowmuch", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionMoreExpensive) {
            msgs.convertAndSend("/topic/" + getId() + "/questionmoreexpensive", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionWhichOne) {
            msgs.convertAndSend("/topic/" + getId() + "/questionwhichone", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionInsteadOf) {
            msgs.convertAndSend("/topic/" + getId() + "/questioninsteadof", getQuestions().get(questionNumber));
        } else throw new IllegalStateException();
    }

    /**
     * Lets the gameInstance go to the next question, and sending this question to the client.
     */
    private void nextQuestion() {
        setState(GameState.INQUESTION);
        if (questionTask != null) questionTask.cancel();
        questionNumber++;
        if(questionNumber == 10){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    msgs.convertAndSend("/topic/" + getId() + "/MPgameMiddle", getPlayers());
                    try {
                        Thread.sleep(5000);
                    }catch (InterruptedException e) {
                        System.out.println("Something went wrong with thread at line 130 : GameInstanceServer");
                    }

                    sendQuestion(questionNumber);
                    startingTime = System.currentTimeMillis();
                    answers.clear();
                    questionTask = new TimerTask() {
                        @Override
                        public void run() {
                            postQuestion();
                        }
                    };
                    questionTimer.schedule(questionTask, questionTime);
                }
            });
        thread.start();
        } else
        if (questionNumber > 19) {
            msgs.convertAndSend("/topic/" + getId() + "/MPgameOver", getPlayers());
        } else {
            sendQuestion(questionNumber);
            startingTime = System.currentTimeMillis();
            answers.clear();
            questionTask = new TimerTask() {
                    @Override
                    public void run() {
                        postQuestion();
                    }
                };
            questionTimer.schedule(questionTask, questionTime);
        }
    }

    /**
     * Sends the post-question state to all clients and goes to the next question after n seconds.
     */
    public void postQuestion() {
        questionTask.cancel();
        setState(GameState.POSTQUESTION);
        msgs.convertAndSend("/topic/" + getId() + "/postquestion", getCurrentQuestion().getCorrectAnswer());
        startingTime = System.currentTimeMillis();
        questionTask = new TimerTask() {
            @Override
            public void run() {
                nextQuestion();
            }
        };
        questionTimer.schedule(questionTask, postQuestionTime);
    }

    /**
     * Returns the time left for the question or post-question
     * @return
     */
    public int getTimeLeft() {
        int timeSpent = (int) (System.currentTimeMillis() - startingTime);
        if (getState() == GameState.POSTQUESTION) return (postQuestionTime - timeSpent);
        return Math.max(questionTime - timeSpent, 0);
    }

    public Question getCurrentQuestion() {
        return getQuestions().get(questionNumber);
    }


    public long getCorrectAnswer() {
        return getQuestions().get(questionNumber).getAnswer();
    }


    public void updatePlayerList() {
        msgs.convertAndSend("/topic/" + getId() + "/players", getPlayers().size());
    }

    /**
     * Receives the answer from a player and saves this temporarily.
     * @param player PLayer which sent the answer
     * @param answer The answer
     * @return True if answer has been taken into, false otherwise
     */
    public boolean answerQuestion(SimpleUser player, Answer answer) {
        if (answers.stream()
                .map(x -> x.getPlayer().getName())
                .noneMatch(x -> x.equals(player.getName()))) {
            answers.add(new ServerAnswer(answer.getAnswer(), player));
            if (answers.size() == getPlayers().size()) {
                postQuestion();
            }
            logger.info("[GI " + getId() + "] Answer received from " + player.getName() + " = " + answer.getAnswer());
            return true;
        }
        return false;
    }

    /**
     * Sends emoji to all connected clients
     * @param emoji Emoji to show
     */
    public void sendEmoji(Emoji emoji) {
        msgs.convertAndSend("/topic/" + getId() + "/emoji", emoji);
    }

    /**
     * A player has used time-reducing powerUp. Send it to all the players from their game.
     *
     * @param timePU powerUp used
     */
    public void decreaseTime(TimePU timePU) {
        if (getTimeLeft() > 1) {
            msgs.convertAndSend("/topic/" + getId() + "/decrease-time", timePU);
        }
    }

    /**
     * A player has used the double-points powerUp. Send it to all the players from their game.
     *
     * @param pointsPU powerUp used
     */
    public void doublePoints(PointsPU pointsPU) {
        msgs.convertAndSend("/topic/" + getId() + "/double-points", pointsPU);
    }

    /**
     * A player has used the remove-answer powerUp. Send it to all the players from their game.
     *
     * @param answerPU powerUp used
     */
    public void removeAnswer(AnswerPU answerPU) {
        msgs.convertAndSend("/topic/" + getId() + "/remove-incorrect-answer", answerPU);
    }

    public boolean disconnectPlayer(SimpleUser player) {
        boolean status = getPlayers().remove(player);
        msgs.convertAndSend("/topic/" + getId() + "/disconnectplayer", player);
        updatePlayerList();
        if (getState() != GameState.INLOBBY && getPlayers().isEmpty()) {
            stopGameInstance();
        }
        return status;
    }

    /**
     * Stops the current gameIstnace, and creates a new one in case this is needed.
     */
    public void stopGameInstance() {
        if (gameController.getServerNames().get(serverName) == getId())
            gameController.createNewMultiplayerLobby(this.serverName);
        try {
            countdownTimer.cancel();
            questionTask.cancel();
            questionTimer.cancel();
        } catch (NullPointerException e) {
            logger.info("Timer has already stopped");
        } finally {
            logger.info("[GI " + getId() + "] GameInstance stopped!");
        }
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GameInstanceServer that = (GameInstanceServer) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(questionNumber, that.questionNumber)
                .append(questionTime, GameInstanceServer.questionTime).append(gameController, that.gameController)
                .append(msgs, that.msgs).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(gameController).append(msgs).append(questionNumber)
                .append(questionTime).toHashCode();
    }
}
