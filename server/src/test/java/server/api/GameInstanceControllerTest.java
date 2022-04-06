package server.api;

import commons.GameInstance;
import communication.RequestToJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class GameInstanceControllerTest {

    private TestActivityRepository activityRepository;
    private SimpMessagingTemplate msgs;
    private GameController gameController;
    private List<GameInstanceServer> gameInstances;
    private GameInstanceController sut;
    private String mainCookie;

    @BeforeEach
    public void initController() {
        gameController = this.initGameController();
        sut = new GameInstanceController(gameController);
        this.gameInstances = sut.getGameInstances();
        this.mainCookie = "ec04009d98eb9e994d7563480477693c";
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
    public void getQuestionTest() {
        var actual = sut.getQuestion(0, 15, mainCookie);
        assertEquals(FORBIDDEN, actual.getStatusCode());
    }


    public GameController initGameController() {
        activityRepository = new TestActivityRepository();
        msgs = null;
        ActivityController activityController = new ActivityController(null, activityRepository);
        GameController gameCtrl = new GameController(msgs, activityController);
        gameCtrl.addPlayer(new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Marcin", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Joshua", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Sophie", "default", GameInstance.MULTI_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Vlad", null, GameInstance.SINGLE_PLAYER));
        gameCtrl.addPlayer(new RequestToJoin("Rafael", null, GameInstance.SINGLE_PLAYER));
        return gameCtrl;
    }
}
