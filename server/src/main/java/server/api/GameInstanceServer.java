package server.api;

import commons.Activity;
import commons.GameInstance;
import commons.GameState;
import commons.player.Player;
import commons.player.ServerAnswer;
import commons.player.SimpleUser;
import commons.question.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class GameInstanceServer extends GameInstance {

    GameController gameController;
    SimpMessagingTemplate msgs;
    int questionNumber = 1;
    StopWatch stopWatch;
    int questionTime = 12000;
    private List<ServerAnswer> answers;
    private long startingTime;
    Logger logger = LoggerFactory.getLogger(GameInstanceServer.class);

    public GameInstanceServer(int id, int type, GameController controller, SimpMessagingTemplate msgs) {
        super(id, type);
        this.gameController = controller;
        this.msgs = msgs;
        stopWatch = new StopWatch();
        answers = new ArrayList<>();
    }

    public void startCountdown() {
        setState(GameState.STARTING);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int time = 6;

            @Override
            public void run() {
                time--;
                msgs.convertAndSend("/topic/" + getId() + "/time", time);
                if (time == 0) {
                    timer.cancel();
                    //TODO START GAME
                    startGame(gameController.activityController.getRandom60().getBody());
                }
            }
        }, 0, 1000);
    }

    @Async
    public void startGame(List<Activity> activities) {
        gameController.createNewMultiplayerLobby();
        setState(GameState.INQUESTION);
        generateQuestions(activities);
        nextQuestion();
    }

    private void goToQuestion(int questionNumber) {
        Question currentQuestion = getQuestions().get(questionNumber);
        logger.info("Question " + questionNumber + " sent" + currentQuestion);
        if (currentQuestion instanceof QuestionHowMuch) {
            msgs.convertAndSend("/topic/" + getId() + "/questionhowmuch", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionMoreExpensive) {
            msgs.convertAndSend("/topic/" + getId() + "/questionmoreexpensive", getQuestions().get(questionNumber));
        } else if (currentQuestion instanceof QuestionWhichOne) {
            msgs.convertAndSend("/topic/" + getId() + "/questionwhichone", getQuestions().get(questionNumber));
        } else throw new IllegalStateException();
    }

    private void nextQuestion() {
        goToQuestion(questionNumber);
        startingTime = System.currentTimeMillis();
        questionNumber++;
        stopWatch.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (questionNumber > 20) {
                    /*TODO CREATE POSTGAME STUFF*/
                    return;
                }
                //TODO ADD POST-QUESTION SCREEN
                answers.clear();
                stopWatch.stop();
                nextQuestion();
            }
        }, 12500);
    }

    public int getTimeLeft() {
        int timeSpent = (int) (System.currentTimeMillis() - startingTime);
//        int timeSpent = (int) stopWatch.getLastTaskTimeMillis();
        return questionTime - timeSpent;
    }

    public Question getCurrentQuestion() {
        return getQuestions().get(questionNumber);
    }


    public long getCorrectAnswer() {
        return getQuestions().get(questionNumber).getAnswer();
    }


    public List<SimpleUser> updatePlayerList() {
        ArrayList<SimpleUser> players = getPlayers()
                .stream().map(SimpleUser.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
        msgs.convertAndSend("/topic/" + getId() + "/players", players);
        return players;
    }

    public boolean answerQuestion(Player player, Answer answer) {
        answers.add(new ServerAnswer(answer.getAnswer(), player));
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GameInstanceServer that = (GameInstanceServer) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(questionNumber, that.questionNumber)
                .append(questionTime, that.questionTime).append(gameController, that.gameController)
                .append(msgs, that.msgs).append(stopWatch, that.stopWatch).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
                .append(gameController).append(msgs).append(questionNumber).append(stopWatch)
                .append(questionTime).toHashCode();
    }
}
