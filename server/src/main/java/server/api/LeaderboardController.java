package server.api;


import commons.player.SimpleUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.LeaderboardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

     private final LeaderboardRepository leaderboardRepository;


    public LeaderboardController(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }


    /**
     * Gets all entries in the leaderboard repository
     * @return a list of Simple Users that are all the entries in the global leaderboard
     */
    @GetMapping(path = {"", "/"})
    public List<SimpleUser> getAll() {
        return leaderboardRepository.findAll();
    }


    /**
     * Adds a player to the global leaderboard repository
     * @param player the player to be added to the leaderboard repository
     * @return the player that was added to the leaderboard repository
     */
    @PostMapping(path = {"/addPlayer"})
    public ResponseEntity<SimpleUser> addPlayer(@RequestBody SimpleUser player) {
        if (isNullOrEmpty(player.getName())) {
            return ResponseEntity.badRequest().build();
        }
        SimpleUser savedPlayer = leaderboardRepository.save(player);
        return ResponseEntity.ok(savedPlayer);
    }


    /**
     * Determines if a certain player name doesn't exist
     * @param s the name of the player
     * @return true, if the string is null or empty, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


    /**
     * Deletes all entries in the leaderboard repository
     */
    @DeleteMapping("/all")
    public ResponseEntity<SimpleUser> deleteAll() {
        try {
            leaderboardRepository.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
