package commons;

import java.util.Objects;

public class QuestionMoreExpensive extends Question {

    private Activity[] activities = new Activity[3];

    public QuestionMoreExpensive(Activity[] activities) {
        this.setTitle("What requires more energy?");
        this.activities = activities;
    }

    public Activity[] getActivities() {
        return this.activities;
    }

    public QuestionMoreExpensive() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionMoreExpensive that = (QuestionMoreExpensive) o;
        return Objects.equals(activities, that.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), activities);
    }

    @Override
    public String toString() {
        return "QuestionMultipleChoice{" +
                super.toString() +
                ", answers=" + activities +
                '}';
    }

    public long getAnswer(){
        long max = 0;
        if(activities[0].getConsumption_in_wh()>max) max = activities[0].getConsumption_in_wh();
        if(activities[1].getConsumption_in_wh()>max) max = activities[1].getConsumption_in_wh();
        if(activities[2].getConsumption_in_wh()>max) max = activities[2].getConsumption_in_wh();
        return max;
    }


}
