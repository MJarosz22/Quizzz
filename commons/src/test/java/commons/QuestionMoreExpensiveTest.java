package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionMoreExpensiveTest {


    @Test
    public void checkConstructor() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 0);
        assertNotNull(q);
    }

    @Test
    public void getTitleTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 1);
        String title = "What requires more energy?";
        assertEquals(title, q.getTitle());
    }

    @Test
    public void setTitleTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 2);
        String anotherTitle = "I've changed the title";
        q.setTitle(anotherTitle);
        assertEquals(anotherTitle, q.getTitle());
    }

    @Test
    public void getActivitiesTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 0);
        assertEquals(q.getActivities(), activities);
    }

    @Test
    public void setActivitiesTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 1);
        Activity[] sameActivitiesDifferentOrder = new Activity[]{activity2, activity3, activity1};

        q.setActivities(sameActivitiesDifferentOrder);
        assertEquals(q.getActivities(), sameActivitiesDifferentOrder);
    }

    @Test
    public void getAnswerTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 2);
        long answer = q.getAnswer();
        assertEquals(8L, answer);
    }

    @Test
    public void getCorrectAnswerTest() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 2);
        assertEquals(3L, q.getCorrectAnswer());

    }

    @Test
    public void equalsHashCode() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q1 = new QuestionMoreExpensive(activities, 0);
        QuestionMoreExpensive q2 = new QuestionMoreExpensive(activities, 0);

        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        Activity[] sameActivitiesDifferentOrder = new Activity[]{activity2, activity3, activity1};
        QuestionMoreExpensive q1 = new QuestionMoreExpensive(activities, 1);
        QuestionMoreExpensive q2 = new QuestionMoreExpensive(sameActivitiesDifferentOrder, 2);

        assertNotEquals(q1, q2);
        assertNotEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    public void hasToString() {
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "00/test.png", "title3", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "00/test.png", "title3", 8L, "https://www.brightspace.com"
        );

        Activity[] activities = new Activity[]{activity1, activity2, activity3};
        QuestionMoreExpensive q = new QuestionMoreExpensive(activities, 1);
        String s = q.toString();

        assertTrue(s.contains(QuestionMoreExpensive.class.getSimpleName()));
        assertTrue(s.contains("title="));
        assertTrue(s.contains("activities="));
        assertTrue(s.contains(q.getActivities()[0].toString()));
        assertTrue(s.contains(q.getActivities()[1].toString()));
        assertTrue(s.contains(q.getActivities()[2].toString()));
    }
}
