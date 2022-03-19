package commons.question;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Answer {


    public Long answer;

    public Answer(Long answer) {
        this.answer = answer;
    }

    public void setAnswer(Long answer) {
        this.answer = answer;
    }

    public Long getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Answer answer1 = (Answer) o;

        return new EqualsBuilder().append(answer, answer1.answer).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(answer).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("answer", answer)
                .toString();
    }
}
