package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionWhichOneTest {


    @Test
    public void checkConstructor() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q = new QuestionWhichOne(activity, 0);
        assertNotNull(q);
    }

    @Test
    public void getTitleTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q = new QuestionWhichOne(activity, 1);
        String title = "How much energy does it take?";
        assertEquals(title, q.getTitle());
    }

    @Test
    public void setTitleTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q = new QuestionWhichOne(activity, 2);
        String anotherTitle = "I've changed the title";
        q.setTitle(anotherTitle);
        assertEquals(anotherTitle, q.getTitle());
    }

    @Test
    public void getActivityTest() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q = new QuestionWhichOne(activity, 0);
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
        QuestionWhichOne q = new QuestionWhichOne(activity, 1);
        q.setActivity(anotherActivity);
        assertEquals(q.getActivity(), anotherActivity);
    }

    @Test
    public void equalsHashCode() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q1 = new QuestionWhichOne(activity, 0);
        QuestionWhichOne q2 = new QuestionWhichOne(activity, 0);
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
        QuestionWhichOne q1 = new QuestionWhichOne(activity, 0);
        QuestionWhichOne q2 = new QuestionWhichOne(anotherActivity, 1);

        assertNotEquals(q1, q2);
        assertNotEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    public void hasToString() {
        Activity activity = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        QuestionWhichOne q = new QuestionWhichOne(activity, 2);
        String s = q.toString();

        assertTrue(s.contains(QuestionWhichOne.class.getSimpleName()));
        assertTrue(s.contains("title="));
        assertTrue(s.contains("activity="));
    }
}
