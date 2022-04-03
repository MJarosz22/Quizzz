package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    @Test
    void setAnswer() {
        Answer answer = new Answer(1500L);
        answer.setAnswer(120L);
        assertEquals(120L, answer.getAnswer());
    }

    @Test
    void getAnswer() {
        Answer answer = new Answer(1500L);
        assertEquals(1500L,answer.getAnswer());
    }

    @Test
    void testEquals() {
        Answer answer1 = new Answer(1500L);
        Answer answer2 = new Answer(1500L);
        assertTrue(answer1.equals(answer2));
    }

    @Test
    void testHashCode() {
        var answer1 = new Answer(1500L);
        var answer2 = new Answer(1500L);
        assertEquals(answer1, answer2);
        assertEquals(answer1.hashCode(), answer2.hashCode());
    }

    @Test
    void testToString() {
        Answer answer1 = new Answer(1500L);
        String string = answer1.toString();
        assertTrue(string.contains("answer=1500"));

    }
}