package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
public class SimpleUser {

    @Id
    @Column(name = "id")
    public long id;

    @Column(name = "")
    public int gameInstanceId;

    @Column(name = "name")
    public String name;

    @Column(name = "score")
    public int score;

    @Column(name = "cookie")
    public String cookie;


    public SimpleUser(){

    }

    public SimpleUser(String name, long id, String cookie) {
        this.name = name;
        this.id = id;
        this.cookie = cookie;
    }

    public SimpleUser(String name, long id, int score, int gameInstanceId, String cookie) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.gameInstanceId = gameInstanceId;
        this.cookie = cookie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleUser that = (SimpleUser) o;

        return new EqualsBuilder().append(id, that.id).append(score, that.score).append(name, that.name).append(cookie, that.cookie).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(score).append(cookie).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("gameInstanceId", gameInstanceId)
                .append("name", name)
                .append("score", score)
                .append("cookie", cookie)
                .toString();
    }

    public Player toPlayer(GameInstance gameInstance){
        return new Player(id, name, gameInstance, cookie);
    }

    public SimpleUser unsafe(){
        return new SimpleUser(name, id, score, gameInstanceId, null);
    }

}
