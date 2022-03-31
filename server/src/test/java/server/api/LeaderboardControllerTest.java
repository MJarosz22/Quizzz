package server.api;

import commons.player.SimpleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

class LeaderboardControllerTest {

    private TestLeaderboardRepository repo;

    private LeaderboardController sut;

    @BeforeEach
    public void setup() {
        repo = new TestLeaderboardRepository();
        sut = new LeaderboardController(repo);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.addPlayer(new SimpleUser(null, 0));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }


    @Test
    public void addPlayerTest() {
        sut.addPlayer(new SimpleUser("TestPlayer", 50));
        assertEquals(true, repo.calledMethods.contains("save"));
    }


    @Test
    public void getAllTest() {
        sut.addPlayer(new SimpleUser("TestPlayer", 50));
        sut.addPlayer(new SimpleUser("B", 340));
        sut.addPlayer(new SimpleUser("C", 290));
        List<SimpleUser> players = sut.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void deleteAllTest() {
        var actual = sut.deleteAll();
        assertEquals(OK, actual.getStatusCode());
    }
}