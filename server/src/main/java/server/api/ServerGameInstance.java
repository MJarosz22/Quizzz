package server.api;

import commons.Activity;
import commons.GameInstance;
import commons.GameState;
import commons.question.Answer;
import commons.question.Question;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServerGameInstance extends GameInstance{

    GameController gameController;
    SimpMessagingTemplate msgs;
    int questionNumber = 1;
    StopWatch stopWatch;
    int questionTime = 8000;

    public ServerGameInstance(int id, int type, GameController controller, SimpMessagingTemplate msgs) {
        super(id, type);
        this.gameController = controller;
        this.msgs = msgs;
        stopWatch = new StopWatch();
    }

    public void startCountdown(){
        setState(GameState.STARTING);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int time = 6;
            @Override
            public void run() {
                time--;
                msgs.convertAndSend("/topic/time", time);
                if(time == 0) {
                    timer.cancel();
                    //TODO START GAME
                    startGame(gameController.activityController.getRandom60().getBody());
                }
            }
        }, 0, 1000);
    }

    @Async
    public void startGame(List<Activity> activities){
        setState(GameState.INQUESTION);
        generateQuestions(activities);
        nextQuestion();
    }

    private void goToQuestion(int questionNumber){
        msgs.convertAndSend("/topic/" + getId() + "/question", getQuestions().get(questionNumber));
    }

    private void nextQuestion(){
        goToQuestion(questionNumber);
        questionNumber++;
        stopWatch.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(questionNumber > 20) {
                    /*TODO CREATE POSTGAME STUFF*/
                    return;
                }
                //TODO ADD POST-QUESTION SCREEN
                nextQuestion();
            }
        }, 8000);
    }

    public int getTimeLeft(){
        int timeSpent = (int) stopWatch.getLastTaskTimeMillis();
        return questionTime - timeSpent;
    }

    public Question getCurrentQuestion(){
        return getQuestions().get(questionNumber);
    }


    public long getCorrectAnswer(){
        return getQuestions().get(questionNumber).getAnswer();
    }

}
