package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Test changeActivity and changeActivityTitle methods
public class QuestionInsteadOfTest {

    private QuestionInsteadOf q;
    private Activity[] activities;
    private Activity mainActivity;
    private Activity changeActivity;

    @BeforeEach
    public void init() {
        mainActivity = new Activity(
                "Activity-ID-0", "00/shower.png", "I take a shower", 100L, "https://www.netflix.com"
        );
        Activity activity1 = new Activity(
                "Activity-ID-1", "00/test.png", "title1", 6L, "https://www.google.com"
        );
        Activity activity2 = new Activity(
                "Activity-ID-2", "01/test.png", "title2", 7L, "https://www.wiki.com"
        );
        Activity activity3 = new Activity(
                "Activity-ID-3", "02/test.png", "title3", 8L, "https://www.brightspace.com"
        );


        activities = new Activity[]{activity1, activity2, activity3};
        q = new QuestionInsteadOf(mainActivity, activities, 0);
    }


    @Test
    public void emptyConstructorTest() {
        QuestionInsteadOf newQ = new QuestionInsteadOf();
        assertNotNull(newQ);
    }

    @Test
    public void simpleConstructorTest() {
        assertNotNull(q);
    }

    @Test
    public void getActivityTest() {
        assertEquals(mainActivity, q.getActivity());
    }

    @Test
    public void setActivityTest() {
        q.setActivity(activities[1]);
        assertEquals(activities[1], q.getActivity());
    }

    @Test
    public void getActivitesTest() {
        assertTrue(Arrays.equals(activities, q.getActivities()));
    }

    @Test
    public void setActivitiesTest() {
        Activity[] newActivities = new Activity[]{activities[1], activities[2], activities[0]};
        q.setActivities(newActivities);
        assertTrue(Arrays.equals(newActivities, q.getActivities()));
    }

    @Test
    public void correctAnswerTest() {
        q.setCorrectAnswer(1);
        assertEquals(1, q.getCorrectAnswer());
    }

    @Test
    public void getAnswerTest() {
        q.setCorrectAnswer(1);
        assertEquals(6L, q.getAnswer());
    }

    @Test
    public void getAnswersTest() {
        String[] result = new String[]{q.getActivities()[0].getTitle(),
                q.getActivities()[1].getTitle(),
                q.getActivities()[2].getTitle()};
        assertTrue(Arrays.equals(result, q.getAnswers()));
    }

    @Test
    public void equalsSameTest() {
        assertEquals(q, q);
        assertEquals(q.hashCode(), q.hashCode());
    }

    @Test
    public void equalsTest() {
        QuestionInsteadOf qCopy = new QuestionInsteadOf(mainActivity, activities, 0);
        assertEquals(q, qCopy);
    }

    @Test
    public void equalsFailedTest() {
        Activity[] newActivities = new Activity[]{activities[1], activities[2], activities[0]};
        QuestionInsteadOf q1 = new QuestionInsteadOf(mainActivity, newActivities, 0);
        assertNotEquals(q, q1);
        assertNotEquals(q.hashCode(), q1.hashCode());
    }

    @Test
    public void equalsNullOrDifferentClassesTest() {
        QuestionHowMuch q1 = null;
        assertNotEquals(q, q1);
        q1 = new QuestionHowMuch(mainActivity, 2);
        assertNotEquals(q, q1);
        assertNotEquals(q.hashCode(), q1.hashCode());
    }

    @Test
    public void toStringTest() {
        String s = q.toString();
        assertTrue(s.contains("title="));
        assertTrue(s.contains("activities="));
    }


     /*
       @Test
    public void changeActivityTest() {
        changeActivity =  new Activity(
                "Activity-ID-changed", "04/test.png", "Boil wate rfor 5 seconds", 1L, "https://www.brightspace.com"
        );
    }

     @Test
    public void changeActivityTitleTest() {

    }
    */

}
