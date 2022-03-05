package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long activityID;

    private String id; //id provided by activity-bank (i.e. "00-shower")

    private String image_path;
    private String title;
    private int consumption_in_wh;
    private String source;

    @SuppressWarnings("unused")
    public Activity() {

    }

    public Activity(String id, String image_path, String title, int consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        if(image_path == null) image_path = "";
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return new EqualsBuilder()
                .append(activityID, activity.activityID)
                .append(consumption_in_wh, activity.consumption_in_wh)
                .append(id, activity.id)
                .append(image_path, activity.image_path)
                .append(title, activity.title)
                .append(source, activity.source)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(activityID)
                .append(id)
                .append(image_path)
                .append(title)
                .append(consumption_in_wh)
                .append(source)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("activityID", activityID)
                .append("id", id)
                .append("image_path", image_path)
                .append("title", title)
                .append("consumption_in_wh", consumption_in_wh)
                .append("source", source)
                .toString();
    }

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(int consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
