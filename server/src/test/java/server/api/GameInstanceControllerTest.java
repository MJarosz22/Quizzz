package server.api;

import commons.*;
import commons.powerups.AnswerPU;
import commons.powerups.PointsPU;
import commons.powerups.TimePU;
import communication.RequestToJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

public class GameInstanceControllerTest {

    private TestActivityRepository activityRepository;
    private SimpMessagingTemplate msgs;
    private GameController gameController;
    private List<GameInstanceServer> gameInstances;
    private GameInstanceController sut;
    private String mainCookie;
    private Question q1, q2, q3;
    private Activity[] activities;
    private String firstCookie;
    private GameInstanceServer firstGI;

    @BeforeEach
    public void initController() {
        gameController = this.initGameController();
        sut = new GameInstanceController(gameController);
        this.gameInstances = sut.getGameInstances();
        this.mainCookie = "ec04009d98eb9e994d7563480477693c";
        this.firstGI = gameInstances.get(0);
        this.firstCookie = gameInstances.get(0).getPlayers().get(0).getCookie();
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
    }

    @Test
    public void getQuestionBadRequestTest() {
        var actual = sut.getQuestion(-1, 20, mainCookie);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getQuestionForbiddenTest() {
        var actual = sut.getQuestion(0, 15, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void getQuestionOKTest() {
        int GIId = firstGI.getId();
        System.out.println(GIId + " " + firstCookie);
        List<Question> listOfQuestions = new ArrayList<>();
        initQuestions();
        listOfQuestions.add(q1);
        listOfQuestions.add(q2);
        listOfQuestions.add(q3);
        firstGI.setQuestions(listOfQuestions);
        var actual = sut.getQuestion(GIId, 0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getPlayersNullTest() {
        var actual = sut.getPlayers(0, mainCookie);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getPlayersTest() {
        var actual = sut.getPlayers(0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void disconnectNullTest() {
        var actual = sut.disconnect(0, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void disconnectTest() {
        var actual = sut.disconnect(0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getQuestionTest() {
        var actual = sut.getQuestion(0, 15, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void getQTypeNullTest() {
        var actual = sut.getCurrentQType(10); // out of range
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getQTypeTest() {
        // No current question as the game has not started yet.
        assertThrows(NullPointerException.class, () -> {
            sut.getCurrentQType(0);
        });
    }

    @Test
    public void otherGetQuestionForbiddenTest() {
        var actual = sut.getQuestion(0, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void otherGetQuestionNOT_FOUNDTest() {
        var actual = sut.getQuestion(0, firstCookie);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void otherGetQuestionOKTest() {
        firstGI.setState(GameState.INQUESTION);
        assertThrows(NullPointerException.class, () -> {
                    var actual = sut.getQuestion(0, firstCookie);
                    assertEquals(OK, actual.getStatusCode());
                }
        );
    }

    @Test
    public void getTimeLeftForbiddennTest() {
        var actual = sut.getTimeLeft(0, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void getTimeLeftNOT_FOUNDTest() {
        var actual = sut.getTimeLeft(0, firstCookie);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }


    @Test
    public void getTimeLeftNOKTest() {
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.getTimeLeft(0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void answerTestForbiddenTest() {
        var actual = sut.answerQuestion(0, null, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }


    @Test
    public void answerTestOkInQuestionTest() {
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.answerQuestion(0, new Answer(1L), firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void answerTestOkStartingTest() {
        firstGI.setState(GameState.STARTING);
        var actual = sut.answerQuestion(0, new Answer(1L), firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getCorrectAnswerForbiddenTest() {
        var actual = sut.getCorrectAnswer(0, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void getCorrectAnswerInQuestionTest() {
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.getCorrectAnswer(0, firstCookie);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void getCorrectAnswerOKPostQuestionTest() {
        firstGI.setState(GameState.POSTQUESTION);
        assertThrows(NullPointerException.class, () -> sut.getCorrectAnswer(0, firstCookie));
    }

    @Test
    public void startGameSamePlayerTest() {
        firstGI.setState(GameState.STARTING);
        var actual = sut.startGame(0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void startGameAnotherPlayerTest() {
        var actual = sut.startGame(0, firstCookie);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void startGameFailTest() {
        var actual = sut.startGame(2, firstCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void sendEmojiStartingTest() {
        firstGI.setState(GameState.STARTING);
        var actual = sut.sendEmoji(0, firstCookie, null);
        assertEquals(OK, actual.getStatusCode());

    }

    @Test
    public void sendEmojiForbiddenTest() {
        Emoji emoji = new Emoji("crying", firstGI.getPlayers().get(0));
        var actual = sut.sendEmoji(0, mainCookie, emoji);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void sendEmojiPlayerTest() {
        Emoji emoji = new Emoji("crying", firstGI.getPlayers().get(0));
        var actual = sut.sendEmoji(0, firstCookie, emoji);
        assertEquals(OK, actual.getStatusCode());

    }

    @Test
    public void getServerNameFailsTest() {
        var actual = sut.getServerName(12); // out of range
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getServerNamETest() {
        var actual = sut.getServerName(0); // out of range
        assertEquals(OK, actual.getStatusCode());
        assertEquals("default", actual.getBody());
    }

    @Test
    public void decreaseTimeImpossibleTest() {
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.decreaseTime(0, firstCookie, null);
        assertEquals(FORBIDDEN, actual.getStatusCode());

    }

    @Test
    public void decreaseTimeInvalidPlayerTest() {
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.decreaseTime(3, mainCookie, null);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void decreaseTimeTest() {
        String playerName = "Petra";
        TimePU time = new TimePU(firstCookie, playerName, 50);
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.decreaseTime(0, firstCookie, time);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void removeAnswerImpossibleTest() {
        AnswerPU answerPU = new AnswerPU(firstCookie, "Petra");
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.removeAnswer(0, firstCookie, answerPU);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void removeAnswerInvalidPlayerTest() {
        AnswerPU answerPU = new AnswerPU(firstCookie, "Petra");
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.removeAnswer(3, mainCookie, answerPU);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void removeAnswerTest() {
        AnswerPU answerPU = new AnswerPU(firstCookie, "Petra");
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.removeAnswer(0, firstCookie, answerPU);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void doublePointsImpossibleTest() {
        PointsPU pointsPU = new PointsPU(firstCookie, "Petra");
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.doublePoints(0, firstCookie, pointsPU);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void doublePointsInvalidPlayerTest() {
        PointsPU pointsPU = new PointsPU(firstCookie, "Petra");
        firstGI.setState(GameState.POSTQUESTION);
        var actual = sut.doublePoints(3, mainCookie, pointsPU);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }

    @Test
    public void doublePointsTest() {
        PointsPU pointsPU = new PointsPU(firstCookie, "Petra");
        firstGI.setState(GameState.INQUESTION);
        var actual = sut.doublePoints(0, firstCookie, pointsPU);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void gameInstanceTypeFailTest() {
        var actual = sut.gameInstanceType(-1);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void gameInstanceTypeTest() {
        var actual = sut.gameInstanceType(3);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(GameInstance.SINGLE_PLAYER, actual.getBody());
    }

    // ----------------------------------------ADDITIONAL REMARKS ----------------------------------------
    public GameController initGameController() {
        activityRepository = new TestActivityRepository();
        SimpMessagingTemplate msgs = Mockito.mock(SimpMessagingTemplate.class);
        ActivityController activityController = new ActivityController(null, activityRepository);
        GameController gameCtrl = new GameController(msgs, activityController);
        gameCtrl.addPlayer(new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER)); //1st
        gameCtrl.addPlayer(new RequestToJoin("Marcin", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Joshua", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Sophie", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Vlad", null, GameInstance.SINGLE_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Rafael", null, GameInstance.SINGLE_PLAYER));
        return gameCtrl;
    }

    public void initQuestions() {

        int defaultNumber = 1;
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        activities = new Activity[]{activity1, activity2, activity3};
        q1 = new QuestionMoreExpensive(activities, defaultNumber);
        q2 = new QuestionHowMuch(activity1, defaultNumber);
        q3 = new QuestionWhichOne(activity1, defaultNumber);
    }

}
