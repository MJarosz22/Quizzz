package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


import javax.persistence.*;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long activityID;

    public String title;
    public int consumption;
    public String source;

    private String image_path;

    private String title;

    private Long consumption_in_wh; //some values in the JSON file are outside the Integer length, so we have to use long

    private String source;

    @SuppressWarnings("unused")
    public Activity() {

    }

    public Activity(String id, String image_path, String title, Long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        if(image_path == null) image_path = "";
        this.title = title;
        this.consumption = consumption;
        this.source = source;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "id")
    public String getId() {
        return this.id;
    }

    @Column(name = "image_path")
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "consumption_in_wh")
    public Long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(Long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "activityID")
    public Long getActivityID() {
        return this.activityID;
    }

    public void setActivityID(Long activityID) {
        this.activityID = activityID;
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




}
