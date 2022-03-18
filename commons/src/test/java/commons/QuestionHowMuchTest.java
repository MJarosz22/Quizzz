package commons;

import commons.question.QuestionHowMuch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionHowMuchTest {


    @Test
    public void checkConstructor() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        assertNotNull(q);
    }

    @Test
    public void getTitleTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        String title = "How much energy does it take?";
        assertEquals(title, q.getTitle());
    }

    @Test
    public void setTitleTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        String anotherTitle = "I've changed the title";
        q.setTitle(anotherTitle);
        assertEquals(anotherTitle, q.getTitle());
    }

    @Test
    public void getActivityTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        assertEquals(q.getActivity(), activity);
    }

    @Test
    public void setActivityTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        Activity anotherActivity = new Activity(
                "Another-activity-ID", "01/test.png", "otherTitle", 12L, "https://www.wiki.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        q.setActivity(anotherActivity);
        assertEquals(q.getActivity(), anotherActivity);
    }

    @Test
    public void equalsHashCode() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q1 = new QuestionHowMuch(activity);
        QuestionHowMuch q2 = new QuestionHowMuch(activity);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        Activity anotherActivity = new Activity(
                "Another-activity-ID", "01/test.png", "otherTitle", 12L, "https://www.wiki.com"
        );
        QuestionHowMuch q1 = new QuestionHowMuch(activity);
        QuestionHowMuch q2 = new QuestionHowMuch(anotherActivity);

        assertNotEquals(q1, q2);
        assertNotEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    public void hasToString() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionHowMuch q = new QuestionHowMuch(activity);
        String s = q.toString();

        assertTrue(s.contains(QuestionHowMuch.class.getSimpleName()));
        assertTrue(s.contains("title="));
        assertTrue(s.contains("activity="));
    }
}
