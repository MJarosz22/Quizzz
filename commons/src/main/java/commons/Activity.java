package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;
    public int consumption;
    public String source;

    @SuppressWarnings("unused")
    public Activity() {

    }

    public Activity(String title, int consumption, String source) {
        this.title = title;
        this.consumption = consumption;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return new EqualsBuilder()
                .append(id, activity.id)
                .append(consumption, activity.consumption)
                .append(title, activity.title)
                .append(source, activity.source).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(consumption)
                .append(source)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("consumption", consumption)
                .append("source", source)
                .toString();
    }

    public long getId() {
        return id;
    }

}
