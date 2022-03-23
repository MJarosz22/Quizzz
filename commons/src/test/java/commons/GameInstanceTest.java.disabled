package commons;

import commons.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameInstanceTest {
    @Test
    public void checkConstructor() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertNotNull(gameInstance);
    }

    @Test
    public void getIdTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(0, gameInstance.getId());
    }

    @Test
    public void setIdTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        gameInstance.setId(100);
        assertEquals(100, gameInstance.getId());
    }

    @Test
    public void getTypeTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(GameInstance.SINGLE_PLAYER, gameInstance.getType());
    }

    @Test
    public void setTypeTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        gameInstance.setType(GameInstance.MULTI_PLAYER);
        assertEquals(GameInstance.MULTI_PLAYER, gameInstance.getType());
    }

    @Test
    public void getPlayersTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertNotNull(gameInstance.getPlayers());
    }

    @Test
    public void setPlayersTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        Player player1 = new Player(1L, "Vlad", gameInstance, "oneCookie");
        Player player2 = new Player(2L, "Petra", gameInstance, "anotherCookie");
        Player player3 = new Player(3L, "Joshua", gameInstance, "lastCookie");

        List<Player> listOfPlayers = new ArrayList<Player>();

        listOfPlayers.add(player1);
        listOfPlayers.add(player2);
        listOfPlayers.add(player3);

        gameInstance.setPlayers(listOfPlayers);

        assertEquals(player1, listOfPlayers.get(0));
        assertEquals(player2, listOfPlayers.get(1));
        assertEquals(player3, listOfPlayers.get(2));
    }


    @Test
    public void equalsHashCode() {
        GameInstance gameInstance1 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        GameInstance gameInstance2 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertEquals(gameInstance1, gameInstance2);
        assertEquals(gameInstance1.hashCode(), gameInstance2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        GameInstance gameInstance1 = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        GameInstance gameInstance2 = new GameInstance(1, GameInstance.SINGLE_PLAYER);
        assertNotEquals(gameInstance1, gameInstance2);
        assertNotEquals(gameInstance1.hashCode(), gameInstance2.hashCode());
    }

    @Test
    public void hasToString() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        String s = gameInstance.toString();
        assertTrue(s.contains(GameInstance.class.getSimpleName()));
        assertTrue(s.contains("id="));
        assertTrue(s.contains("type="));
        assertTrue(s.contains("players="));
        assertTrue(s.contains("questions="));
    }

    @Test
    public void generateQuestionFailsTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        assertThrows(IllegalArgumentException.class, () -> {
            gameInstance.generateQuestions(new ArrayList<Activity>());
        });
    }

    @Test
    public void generateQuestionTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        List<Activity> activities = this.generateActivities();
        gameInstance.generateQuestions(activities);
        assertTrue(gameInstance.getQuestions().get(0) instanceof Question);
    }

    @Test
    public void getQuestionsTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        List<Activity> activities = this.generateActivities();
        gameInstance.generateQuestions(activities);
        Activity expectedActivity = new Activity("Activity-ID0", "0/test.png", "Title #0", 0L, "https://www.the-same-source.com");
        QuestionWhichOne expectedQuestion = new QuestionWhichOne(expectedActivity);
        assertEquals(expectedQuestion, gameInstance.getQuestions().get(0));
    }

    @Test
    public void setQuestionsTest() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        List<Activity> activities = this.generateActivities();
        gameInstance.generateQuestions(activities);
        List<Question> questions = new ArrayList<>();
        Activity activity = new Activity("Activity-ID0", "0/test.png", "Title #0", 0L, "https://www.the-same-source.com");
        QuestionWhichOne question = new QuestionWhichOne(activity);
        questions.add(question);
        gameInstance.setQuestions(questions);
        assertEquals(gameInstance.getQuestions().size(), 1);
        assertEquals(gameInstance.getQuestions().get(0), question);
    }

    @Test
    public void questionsTestHardcode() {
        GameInstance gameInstance = new GameInstance(0, GameInstance.SINGLE_PLAYER);
        List<Question> questions = new ArrayList<>();
        Activity act1 = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        Activity act2 = new Activity("Activity-ID2", "01/test.png", "Other-title", 12L, "https://www.essay.com");

        Question q1 = new QuestionHowMuch(act1);
        Question q2 = new QuestionWhichOne(act1);
        questions.add(q1);
        questions.add(q2);

        gameInstance.setQuestions(questions);
        assertEquals(2, gameInstance.getQuestions().size());
    }

    //------------------------------------------------ ADDITIONAL METHODS ----------------------------------------------

    public List<Activity> generateActivities() {
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
