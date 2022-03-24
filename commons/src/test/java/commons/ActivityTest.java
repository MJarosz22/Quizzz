package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    @Test
    public void checkConstructor() {
        var q = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        assertNotNull(q);
    }

    @Test
    public void equalsHashCode() {
        var a = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        var b = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        var b = new Activity(
                "Activity-D", "00/test.png", "Title", 6L, "https://www.google.com"
        );
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var q = new Activity(
                "Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com"
        ).toString();
        assertTrue(q.contains(Activity.class.getSimpleName()));
        assertTrue(q.contains("title="));
        assertTrue(q.contains("Title"));
    }

    @Test
    public void getIdTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        assertEquals("Activity-ID", act.getId());
    }

    @Test
    public void setIdTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        act.setId("new-id");
        assertEquals("new-id", act.getId());
    }

    @Test
    public void getImage_pathTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        assertEquals("00/test.png", act.getImage_path());
    }

    @Test
    public void setImage_pathTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        act.setImage_path("01/changed-path.png");
        assertEquals("01/changed-path.png", act.getImage_path());
    }

    @Test
    public void getTitleTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        assertEquals("Title", act.getTitle());
    }

    @Test
    public void setTitleTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        act.setTitle("Different title");
        assertEquals("Different title", act.getTitle());
    }

    @Test
    public void getConsumption_in_whTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        assertEquals(6L, act.getConsumption_in_wh());
    }

    @Test
    public void setConsumption_in_whTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        act.setConsumption_in_wh(100L);
        assertEquals(100L, act.getConsumption_in_wh());
    }

    @Test
    public void getSourceTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        assertEquals("https://www.google.com", act.getSource());
    }

    @Test
    public void setSourceTest() {
        Activity act = new Activity("Activity-ID", "00/test.png", "Title", 6L, "https://www.google.com");
        act.setSource("https://www.brightspace.com");
        assertEquals("https://www.brightspace.com", act.getSource());
    }
}
