package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is an open-question type of question.
 * This question type asks the user to guess the correct amount of kWh that takes for a certain activity.
 * The user will be asked to fill a number regarding his prediction regarding the energy consumed by its activity attribute
 * - activity: the Activity object instance that the user is asked to guess how much energy it consumes.
 * - title: the title of this specific question, may be changed later to "Guess the correct amount of energy it takes
 * to... " so that we do not confuse this class with 'QuestionWhichOne'.
 */
public class QuestionHowMuch extends Question {

    private Activity activity;

    public QuestionHowMuch() {
    }

    public QuestionHowMuch(Activity activity, int number) {
        this.setTitle("How much energy does it take?");
        this.activity = activity;
        setNumber(number);
    }


    /**
     * Getter for the activity the user will be asked to guess how much energy it consumes.
     *
     * @return an Activity object representing the object we are interested in
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Setter for the activity the user will be asked to guess how much energy it consumes.
     *
     * @param activity an Activity object representing the object we are interested in
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Getter for the correct answer to QuestionHowMuch instances.
     * The user will be asked to input a number representing his guess. It will be compared to the correct answer in order to award
     * this player a specific number of partial points.
     *
     * @return a long value representing how much energy a specific activity consumes
     */
    public long getAnswer() {
        return activity.getConsumption_in_wh();
    }

    /**
     * Getter for the correct answer to QuestionHowMuch instances.
     * The user will be asked to input a number representing his guess. It will be compared to the correct answer in order to award
     * this player a specific number of partial points.
     *
     * @return a long value representing how much energy a specific activity consumes
     */
    public long getCorrectAnswer() {
        return activity.getConsumption_in_wh();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionHowMuch q = (QuestionHowMuch) o;

        return new EqualsBuilder()
                .append(getTitle(), q.getTitle())
                .append(activity, q.activity)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getTitle())
                .append(activity)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", getTitle())
                .append("activity", activity)
                .toString();
    }
}
