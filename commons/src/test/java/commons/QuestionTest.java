package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {

    @Test
    public void CheckConstructor(){

        Question question = new QuestionMultipleChoice("Title Test", new ArrayList<>(), new ArrayList<>());
        assertEquals(question.getType(), 1);
        assertEquals(question.getTitle(), "Title Test");
    }
}
