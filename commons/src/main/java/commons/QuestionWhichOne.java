package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Random;

/**
 * This is a multiple-choice type of question.
 * This question type asks the user to choose, for a certain activity, the amount of energy he/she thinks the activity consumes
 * out of 3 options.
 * It is similar to 'QuestionHowMuch' type of question, but this comes up in order to help the user to have more
 * chances of choosing a correct answer (as there will be only 3 options available (expressed in kWh), instead of
 * asking the user to input the exact amount of kWh he/she thinks the activity consumes).
 * - activity: the Activity object instance that the user is asked to guess how much energy it consumes.
 * - title: the title of this specific question.
 */
public class QuestionWhichOne extends Question {

    private Activity activity;

    private long[] answers = new long[3];

    private int correctAnswer;

    public QuestionWhichOne() {
    }

    public QuestionWhichOne(Activity activity, int number) {
        this.setTitle("How much energy does it take?");
        this.activity = activity;
        setNumber(number);
        Random random = new Random();
        correctAnswer = random.nextInt(3);

        for (int i = 0; i < 3; i++) {
            double multiplier = random.nextInt(100) / 100d + 0.5;
            if (i == correctAnswer) {
                int imprecise = random.nextInt((int) (activity.getConsumption_in_wh() / 100) + 1) - (int) (activity.getConsumption_in_wh() / 100);
                answers[i] = activity.getConsumption_in_wh() + imprecise; //TO MAKE CORRECT ANSWER NOT PRECISE TOO
            } else {
                answers[i] = (int) (activity.getConsumption_in_wh() * multiplier);
            }
        }
    }

    /**
     * Getter for the options provided to the user for each instance of QuestionWhichOne.
     *
     * @return a long array of fixed length 3 that represents the possible randomly-generated options, one of which is the
     * correct consumption.
     */
    public long[] getAnswers() {
        return answers;
    }

    /**
     * Setter for the options provided to the user for each instance of QuestionWhichOne.
     *
     * @param answers a long array of fixed length 3 that represents the possible randomly-generated options, one of which is the
     *                correct consumption
     */
    public void setAnswers(long[] answers) {
        this.answers = answers;
    }

    /**
     * Getter for the correct answer from those 3 provided to the user
     *
     * @return long value representing how much energy does a certain activity consumes.
     */
    @Override
    public long getAnswer() {
        return activity.getConsumption_in_wh();
    }

    /**
     * Getter for the INDEX of the correct answer from those 3 provided to the user
     * The position of the correct answer is selected randomly (1st button, 2nd button or the 3rd one) in the constructor
     * so that the game remains interactive and the user cannot decode the pattern.
     *
     * @return a long value (that takes values from 1 to 3),  representing on which button is the correctAnswer placed
     */
    @Override
    public long getCorrectAnswer() {
        return correctAnswer + 1;
    }


    /**
     * Getter for the activity the user will be asked to compare to other 3 activities
     * (whose consumptions are stored in 'answers' array) in order to decide which one matches its consumption
     *
     * @return an Activity object representing the object we are interested in
     */
    public Activity getActivity() {
        return activity;
    }


    /**
     * Getter for the activity the user will be asked to compare to other 3 activities
     * (whose consumptions are stored in 'answers' array) in order to decide which one matches its consumption
     *
     * @param activity an Activity object representing the object we are interested in
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override

    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionWhichOne q = (QuestionWhichOne) o;

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
