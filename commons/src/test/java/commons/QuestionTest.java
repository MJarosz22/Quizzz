package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    public void CheckConstructorMoreExpensive() {

        Question question = new QuestionMoreExpensive("Title Test", new ArrayList<>(), new ArrayList<>());
        assertEquals(question.getType(), 1);
        assertEquals(question.getTitle(), "Title Test");
    }

    @Test
    public void CheckConstructorHowMuchEnergy() {

        Question question = new QuestionHowMuch("Title Test", new ArrayList<>(), new ArrayList<>());
        assertEquals(question.getType(), 2);
        assertEquals(question.getTitle(), "Title Test");
    }
}
