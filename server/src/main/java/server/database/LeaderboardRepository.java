package server.database;

import commons.player.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<SimpleUser, Long>{
}
