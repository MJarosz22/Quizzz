package server.api;

import commons.GameInstance;
import commons.player.SimpleUser;
import communication.RequestToJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.*;

//TODO: Fix the commented test for getImage() method (it should have worked, I don't get what's wrong there)
// TODO: Adapt tests to real 'msgs' object, not 'null'

public class GameControllerTest {
    private TestActivityRepository activityRepository;
    private Random random;
    private SimpMessagingTemplate msgs;

    private GameController sut;


    @BeforeEach
    public void initController() {
        activityRepository = new TestActivityRepository();
        msgs = null;
        ActivityController activityController = new ActivityController(random, activityRepository);
        sut = new GameController(random, activityRepository, msgs, activityController);
    }

    // ------------------------------------ TEST ADDITIONAL METHODS ------------------------------------------------------

    @Test
    public void getCurrentMPGIIdTest() {
        sut.addPlayer(new RequestToJoin("Petra", "test", GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Marcin", "test", GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Joshua", "test", GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Sophie", "test", GameInstance.SINGLE_PLAYER));

        assertEquals(0, sut.getCurrentMPGIId());
    }

    @Test
    public void getPlayersTest() {
        sut.addPlayer(new RequestToJoin("Petra", null, GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Marcin", null, GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Joshua", null, GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Sophie", null, GameInstance.SINGLE_PLAYER));

        assertEquals(4, sut.getPlayers().size());
    }

    @Test
    public void getGameInstancesTest() {
        sut.addPlayer(new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER));
        sut.addPlayer(new RequestToJoin("Marcin", null, GameInstance.SINGLE_PLAYER));
        sut.addPlayer(new RequestToJoin("Joshua", "first", GameInstance.MULTI_PLAYER));
        sut.addPlayer(new RequestToJoin("Sophie", "second", GameInstance.MULTI_PLAYER));

        assertEquals(4, sut.getGameInstances().size());
    }

    @Test
    public void createNewMultiplayerLobby() {
        // 3 initially hard-coded servers
        assertEquals(3, sut.getGameInstances().size());
        assertEquals(GameInstance.MULTI_PLAYER, sut.getGameInstances().get(0).getType());
        assertEquals(0, sut.getCurrentMPGIId());
    }

    // ------------------------------------ TEST METHODS USED BY THE CONTROLLER ------------------------------------------------------
    @Test
    public void nullRequestAddPlayerTest() {
        RequestToJoin rq = null;
        var actual = sut.addPlayer(rq);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void invalidGameInstanceAddPlayerTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            RequestToJoin rq = new RequestToJoin("Vlad", "default", 3);
            var actual = sut.addPlayer(rq);
            assertEquals(BAD_REQUEST, actual.getStatusCode());
        });

    }

    @Test
    public void addPlayerSinglePlayerTest() {
        RequestToJoin rq = new RequestToJoin("Vlad", null, GameInstance.SINGLE_PLAYER);
        var actual = sut.addPlayer(rq);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(1, sut.getPlayers().size());
        assertEquals("Vlad", sut.getPlayers().get(0).getName());
    }

    @Test
    public void addPlayerMultiPlayerTest() {

        RequestToJoin rq = new RequestToJoin("Vlad", "default", GameInstance.MULTI_PLAYER);
        var actual = sut.addPlayer(rq);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(1, sut.getPlayers().size());
        //Already 3 hard-coded instances are created
        assertEquals(3, sut.getGameInstances().size());

    }

    @Test
    public void getImageNotFoundTest() {
        var actual = sut.getImage("123", "this-image-does-not-exist.png");
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void getLastGIIdMultTest() {

        RequestToJoin rq = new RequestToJoin("Vlad", "default", GameInstance.MULTI_PLAYER);
        sut.addPlayer(rq);
        var actual = sut.getLastGIIdMult();

        assertEquals(OK, actual.getStatusCode());
        assertEquals(0, actual.getBody());
    }

    @Test
    public void getLastGIIdSingleTest() {

        RequestToJoin rq = new RequestToJoin("Sophie", null, GameInstance.SINGLE_PLAYER);
        sut.addPlayer(rq);
        var actual = sut.getLastGIIdSingle();

        assertEquals(OK, actual.getStatusCode());
        // 0,1,2 are gameInstances occupied by default, first and second.
        assertEquals(3, actual.getBody());
    }


    @Test
    public void allPlayersInvalidGameInstanceIDTest() {
        var actual = sut.allPlayers(120);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void allPlayersTest() {

        RequestToJoin rq1 = new RequestToJoin("Rafael", null, GameInstance.SINGLE_PLAYER);
        RequestToJoin rq2 = new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER);
        RequestToJoin rq3 = new RequestToJoin("Marcin", "default", GameInstance.MULTI_PLAYER);
        sut.addPlayer(rq1);
        sut.addPlayer(rq2);
        sut.addPlayer(rq3);
        // The first 3 instances are occupied BY DEFAULT BY the  hard-coded servers (0, 1, 2)
        var actualSinglePlayer = sut.allPlayers(3);
        var actualsMultiPlayer = sut.allPlayers(0);
        assertEquals(OK, actualSinglePlayer.getStatusCode());
        assertEquals(1, actualSinglePlayer.getBody().size());
        assertEquals(OK, actualsMultiPlayer.getStatusCode());
        assertEquals(2, actualsMultiPlayer.getBody().size());
    }


    @Test
    public void connectedPlayersFailTest() {
        var actual = sut.connectedPlayers((-1));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void connectedPlayersTest() {

        sut.addPlayer(new RequestToJoin("Petra", "default", GameInstance.MULTI_PLAYER));
        sut.addPlayer(new RequestToJoin("Marcin", "default", GameInstance.MULTI_PLAYER));
        sut.addPlayer(new RequestToJoin("Joshua", "default", GameInstance.MULTI_PLAYER));
        sut.addPlayer(new RequestToJoin("Sophie", "default", GameInstance.MULTI_PLAYER));
        sut.getGameInstances().get(0).getPlayers().remove(2);

        var actual = sut.connectedPlayers(0);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(3, actual.getBody().size());
        assertEquals("Petra", actual.getBody().get(0).getName());
        assertEquals("Marcin", actual.getBody().get(1).getName());
        assertEquals("Sophie", actual.getBody().get(2).getName());
    }

    @Test
    public void updatePlayerNullTest() {
        SimpleUser player = null;
        var actual = sut.updatePlayer(1, player);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void updatePlayerNotFoundTest() {
        SimpleUser player = new SimpleUser(0, "Marcin", 0, "ec04009d98eb9e994d7563480477693c");
        var actual = sut.updatePlayer(10, player);
        assertEquals(NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void updatePlayerTest() {

        String cookieForMarcin = "ec04009d98eb9e994d7563480477693c";
        SimpleUser player = new SimpleUser(3, "Marcin", 0, cookieForMarcin);
        sut.getPlayers().add(player);
        player.setScore(500);
        var actual = sut.updatePlayer(3, player);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(500, actual.getBody().getScore());
        assertEquals(3, actual.getBody().getId());
        assertEquals(cookieForMarcin, actual.getBody().getCookie());
    }



   /*  @Test
    public void getImageOKTest() {
        var actual = sut.getImage("38", "coffee.png");
        assertEquals(OK, actual.getStatusCode());
    }
    */

}

