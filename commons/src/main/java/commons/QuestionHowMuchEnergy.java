package commons;

import java.util.List;
import java.util.Objects;

public class QuestionHowMuchEnergy extends Question {
    private List<Integer> consumptions;

    public QuestionHowMuchEnergy(String title, List<Integer> disabledPowerUps, List<Integer> consumptions) {
        super(2, title, disabledPowerUps);
        this.consumptions = consumptions;
    }

    public QuestionHowMuchEnergy() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionHowMuchEnergy that = (QuestionHowMuchEnergy) o;
        return Objects.equals(consumptions, that.consumptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), consumptions);
    }

    @Override
    public String toString() {
        return "QuestionMultipleChoice{" +
                super.toString() +
                ", consumptions=" + consumptions +
                '}';
    }
}
