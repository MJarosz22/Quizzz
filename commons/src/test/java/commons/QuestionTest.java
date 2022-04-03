package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    private Question q1;
    private Question q2;
    private Question q3;
    private Activity[] activities;

    @BeforeEach
    public void initTests() {

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

    @Test
    public void CheckConstructorMoreExpensive() {

        assertTrue(q1 instanceof QuestionMoreExpensive);
        assertEquals(q1.getTitle(), "What requires more energy?");
    }

    @Test
    public void CheckConstructorHowMuchEnergy() {

        assertTrue(q2 instanceof QuestionHowMuch);
        assertEquals(q2.getTitle(), "How much energy does it take?");
    }

    @Test
    public void CheckToString() {
        String string = q1.toString();
        assertTrue(string.contains("QuestionMoreExpensive"));
        assertTrue(string.contains("title="));
        assertTrue(string.contains("activities="));
        assertTrue(string.contains(activities[0].toString()));
        assertTrue(string.contains(activities[1].toString()));
        assertTrue(string.contains(activities[2].toString()));
    }

    @Test
    public void EqualsMoreExpensive() {
        Question compareToQ = new QuestionMoreExpensive(activities, 1);
        assertEquals(q1, compareToQ);
    }

    @Test
    public void EqualsMoreExpensiveFails() {
        Question compareToQ = new QuestionMoreExpensive(new Activity[]{activities[2], activities[0], activities[1]}, 1);
        assertNotEquals(q1, compareToQ);
    }

    @Test
    public void EqualsHowMuch() {
        Question compareToQ = new QuestionHowMuch(activities[0], 1);
        assertEquals(q2, compareToQ);
    }


    @Test
    public void EqualsHowMuchFails() {
        Question compareToQ = new QuestionHowMuch(activities[2], 1);
        assertNotEquals(q2, compareToQ);
    }

    @Test
    public void EqualsDifferentTypesOfQuestions() {
        assertNotEquals(q1, q2);
    }

    @Test
    public void getNumberTest(){
        assertEquals(1, q1.getNumber());
    }

    @Test
    public void setNumberTest(){
        q1.setNumber(2);
        assertEquals(2, q1.getNumber());
    }

}
