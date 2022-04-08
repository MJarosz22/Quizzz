package server.api;

import commons.Activity;
import commons.GameInstance;
import communication.RequestToJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

public class GameInstanceServerTest {

    private GameInstanceServer sut;
    String serverName;
    SimpMessagingTemplate msgs;
    private List<String> questionTypes;
    private GameController gameCtrl;
    private List<Activity> activities;


    @BeforeEach
    public void initController() {
        gameCtrl = Mockito.mock(GameController.class);
        msgs = Mockito.mock(SimpMessagingTemplate.class);
        serverName = "default";
        activities = generateActivities();
        gameCtrl.addPlayer(new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER));
        sut = new GameInstanceServer(0, GameInstance.MULTI_PLAYER, gameCtrl, msgs, serverName);
        questionTypes = new ArrayList<>();
        questionTypes.add("commons.QuestionInsteadOf");
        questionTypes.add("commons.QuestionWhichOne");
        questionTypes.add("commons.QuestionHowMuch");
        questionTypes.add("commons.QuestionMoreExpensive");
    }

    @Test
    public void generateQuestionExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> sut.generateQuestions(new ArrayList<>()));
    }

    @Test
    public void generateQuestionTest() {
        sut.generateQuestions(activities);
        assertNotNull(sut.getQuestions());
    }

    @Test
    public void startCountdownTest() {
        sut.startCountdown();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                assertNotNull(sut.getQuestions());
            }
        };
        new Timer().schedule(timerTask, 5000);
    }

    @Test
    public void startGameTest() {
        sut.startGame(activities);
        assertNotNull(sut.getQuestions());
    }

    @Test
    public void sendQuestionTest() {
        sut.generateQuestions(activities);
        sut.sendQuestion(0);
        sut.sendQuestion(1);
        sut.sendQuestion(2);
        sut.sendQuestion(3);
        assertTrue(questionTypes.contains(sut.getQuestions().get(0).getClass().getName()));
        assertTrue(questionTypes.contains(sut.getQuestions().get(1).getClass().getName()));
        assertTrue(questionTypes.contains(sut.getQuestions().get(2).getClass().getName()));
        assertTrue(questionTypes.contains(sut.getQuestions().get(3).getClass().getName()));
    }

    @Test
    public void nextQuestionImpossibleTest() {
        sut.generateQuestions(activities);
        for (int i = 0; i < 21; ++i) {
            sut.nextQuestion(); // covers all cases
            if (i == 9) {
                assertEquals("How much energy does it take?", sut.getCurrentQuestion().getTitle());
                assertEquals(22L, sut.getCorrectAnswer()); //
            }
        }
    }

    @Test
    public void equalsTest() {
        assertEquals(sut, sut);
        assertEquals(sut.hashCode(), sut.hashCode());
        assertNotEquals(sut, null);
        assertNotEquals(sut, gameCtrl);
        assertNotEquals(sut.hashCode(), gameCtrl.hashCode());
        GameInstanceServer newSut = new GameInstanceServer(0, GameInstance.MULTI_PLAYER, gameCtrl, msgs, serverName);
        assertEquals(sut, newSut);
        assertEquals(sut.hashCode(), newSut.hashCode());
    }


    // ------------------------------------ADDITIONAL METHODS------------------------------------
    public static List<Activity> generateActivities() {
        List<Activity> activities = new ArrayList<>();
        Activity baseActivity = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        for (int i = 0; i < 60; ++i) {
            String counter = String.valueOf(i);
            Activity generatedActivity = new Activity(
                    baseActivity.getId() + counter,
                    counter + "/test.png",
                    "Title #" + counter,
                    Long.parseLong(counter),
                    "https://www.the-same-source.com"
            );
            activities.add(generatedActivity);
        }

        return activities;
    }
}
