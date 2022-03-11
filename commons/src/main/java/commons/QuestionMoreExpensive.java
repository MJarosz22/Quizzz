package commons;

import java.util.List;
import java.util.Objects;

public class QuestionMoreExpensive extends Question {

    private Activity[] answers;

    public QuestionMoreExpensive(String title, List<Integer> disabledPowerUps, Activity[] answers) {
        super(answers);
        this.answers = answers;
    }


    @Override
    public void setPowerups() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionMoreExpensive that = (QuestionMoreExpensive) o;
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
