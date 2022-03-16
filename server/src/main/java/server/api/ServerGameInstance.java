package server.api;

import commons.*;
import commons.player.Player;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.util.*;

public class ServerGameInstance extends GameInstance{

    GameController gameController;
    SimpMessagingTemplate msgs;

    public ServerGameInstance(int id, int type, GameController controller, SimpMessagingTemplate msgs) {
        super(id, type);
        this.gameController = controller;
        this.msgs = msgs;
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
        goToQuestion(1);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for(int i = 0; i< 1000; i++);
        stopWatch.stop();
    }

    private void goToQuestion(int questionNumber){
        msgs.convertAndSend("/topic/question", getQuestions().get(questionNumber));
    }

}
