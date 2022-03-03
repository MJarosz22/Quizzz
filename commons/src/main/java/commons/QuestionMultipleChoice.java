package commons;

import java.util.List;
import java.util.Objects;

public class QuestionMultipleChoice extends Question {

    private List<Activity> answers;

    public QuestionMultipleChoice(long id, int type, String title, List<Integer> disabledPowerUps, List<Activity> answers) {
        super(id, type, title, disabledPowerUps);
        this.answers = answers;
    }

    public QuestionMultipleChoice() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionMultipleChoice that = (QuestionMultipleChoice) o;
        return Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), answers);
    }

    @Override
    public String toString() {
        return "QuestionMultipleChoice{" +
                super.toString() +
                ", answers=" + answers +
                '}';
    }


}
