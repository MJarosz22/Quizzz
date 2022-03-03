package commons;

import java.util.List;
import java.util.Objects;

public class QuestionHowMuch extends Question {
    private List<Integer> answers;

    public QuestionHowMuch(String title, List<Integer> disabledPowerUps, List<Integer> answers) {
        super(2, title, disabledPowerUps);
        this.answers = answers;
    }

    public QuestionHowMuch() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionHowMuch that = (QuestionHowMuch) o;
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
