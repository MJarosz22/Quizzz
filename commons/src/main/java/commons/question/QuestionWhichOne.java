package commons.question;

import commons.Activity;
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

    public long[] getAnswers() {
        return answers;
    }

    public void setAnswers(long[] answers) {
        this.answers = answers;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private int correctAnswer;

    public QuestionWhichOne(Activity activity, int number) {
        this.setTitle("How much energy does it take?");
        this.activity = activity;
        setNumber(number);
        Random random = new Random();
        correctAnswer = random.nextInt(3);

        for(int i = 0; i < 3; i++){
            double multiplier = random.nextInt(100) / 100d + 0.5;
            if(i == correctAnswer){
                int imprecise = random.nextInt((int)(activity.getConsumption_in_wh() / 100)) - (int)(activity.getConsumption_in_wh() / 100);
                answers[i] = activity.getConsumption_in_wh() + imprecise; //TO MAKE CORRECT ANSWER NOT PRECISE TOO
            }else{
                answers[i] = (int)(activity.getConsumption_in_wh() * multiplier);
            }
        }
    }

    public QuestionWhichOne() {
    }

    @Override
    public long getAnswer() {
        return activity.getConsumption_in_wh();
    }

    @Override
    public long getCorrectAnswer() {
        return correctAnswer + 1;
    }

    public Activity getActivity() {
        return activity;
    }

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
