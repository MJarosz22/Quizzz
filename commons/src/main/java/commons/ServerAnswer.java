package commons;

import commons.player.Player;
import commons.player.SimpleUser;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServerAnswer extends Answer {

    private SimpleUser player;

    public ServerAnswer(Long answer, SimpleUser player) {
        super(answer);
        this.player = player;
    }

    public SimpleUser getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServerAnswer that = (ServerAnswer) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(player, that.player).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(player).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("player", player)
                .append("answer", answer)
                .toString();
    }
}
