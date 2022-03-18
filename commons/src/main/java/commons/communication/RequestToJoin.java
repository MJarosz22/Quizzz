package commons.communication;

import commons.GameInstance;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RequestToJoin {

    private String name;
    private int gameType;

    public RequestToJoin() {
    }

    public RequestToJoin(String name, int gameType) {
        this.name = name;
        if (gameType != GameInstance.SINGLE_PLAYER && gameType != GameInstance.MULTI_PLAYER)
            throw new IllegalArgumentException();
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        if (gameType != GameInstance.SINGLE_PLAYER && gameType != GameInstance.MULTI_PLAYER)
            throw new IllegalArgumentException();
        this.gameType = gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RequestToJoin req = (RequestToJoin) o;

        return new EqualsBuilder()
                .append(name, req.name)
                .append(gameType, req.gameType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(gameType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("gameType", gameType)
                .toString();
    }
}