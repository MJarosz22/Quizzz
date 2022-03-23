package server.api;

import commons.*;
import commons.player.SimpleUser;
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

    GameController gameController;
    SimpMessagingTemplate msgs;
    int questionNumber = -1;
    private final int questionTime = 12000;
    private final int postQuestionTime = 5000;
    private final List<ServerAnswer> answers;
    private long startingTime;
    Logger logger = LoggerFactory.getLogger(GameInstanceServer.class);
    private TimerTask questionTask;
    private final Timer questionTimer;

    private final Timer countdownTimer;

    public GameInstanceServer(int id, int type, GameController controller, SimpMessagingTemplate msgs) {
        super(id, type);
        this.gameController = controller;
        this.msgs = msgs;
        answers = new ArrayList<>();
        questionTimer = new Timer();
        countdownTimer = new Timer();
    }

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

    @Async
    public void startGame(List<Activity> activities) {
        gameController.createNewMultiplayerLobby();
        setState(GameState.INQUESTION);
        generateQuestions(activities);
        nextQuestion();
    }

    private void sendQuestion(int questionNumber) {
        Question currentQuestion = getQuestions().get(questionNumber);
        logger.info("[GI "+ getId() + "] Question " + questionNumber + " sent.");
        if (currentQuestion instanceof QuestionHowMuch) {
            msgs.convertAndSend("/topic/" + getId() + "/questionhowmuch", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionMoreExpensive) {
            msgs.convertAndSend("/topic/" + getId() + "/questionmoreexpensive", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionWhichOne) {
            msgs.convertAndSend("/topic/" + getId() + "/questionwhichone", getQuestions().get(questionNumber));
        } else throw new IllegalStateException();
    }

    private void nextQuestion() {
        setState(GameState.INQUESTION);
        if(questionTask != null) questionTask.cancel();
        questionNumber++;
        if(questionNumber > 20){
            //TODO ADD POST-GAME SCREEN AND FUNCTIONALITY
        }
        sendQuestion(questionNumber);
        startingTime = System.currentTimeMillis();
        answers.clear();
        questionTask = new TimerTask() {
            @Override
            public void run() {
                postQuestion();
//                nextQuestion();
            }
        };
        questionTimer.schedule(questionTask, questionTime);
    }

    public void postQuestion(){
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

    public int getTimeLeft() {
        int timeSpent = (int) (System.currentTimeMillis() - startingTime);
        if(getState() == GameState.POSTQUESTION) return (postQuestionTime - timeSpent);
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

    public boolean answerQuestion(SimpleUser player, Answer answer) {
        if(answers.stream()
                .map(x -> x.getPlayer().getName())
                .noneMatch(x-> x.equals(player.getName()))){
            answers.add(new ServerAnswer(answer.getAnswer(), player));
            if(answers.size() == getPlayers().size()) {
                //TODO POST QUESTION
                postQuestion();
//                nextQuestion();
            }
            logger.info("[GI "+ getId() + "] Answer received from " + player.getName() + " = " + answer.getAnswer());
            return true;
        }
        return false;
    }

    public boolean disconnectPlayer(SimpleUser player){
        boolean status = getPlayers().remove(player);
        updatePlayerList();
        if(getState() != GameState.INLOBBY && getPlayers().isEmpty()){
            stopGameInstance();
        }
        return status;
    }

    public void stopGameInstance(){
        if(gameController.getCurrentMPGIId() == getId())
            gameController.createNewMultiplayerLobby();
        countdownTimer.cancel();
        questionTask.cancel();
        questionTimer.cancel();
        logger.info("[GI " + getId() + "] GameInstance stopped!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GameInstanceServer that = (GameInstanceServer) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(questionNumber, that.questionNumber)
                .append(questionTime, that.questionTime).append(gameController, that.gameController)
                .append(msgs, that.msgs).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(gameController).append(msgs).append(questionNumber)
                .append(questionTime).toHashCode();
    }
}
