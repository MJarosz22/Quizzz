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

    @GetMapping(path = {"", "/"})
    public List<SimpleUser> getAll() {
        List<SimpleUser> players = leaderboardRepository.findAll();
        return players;
    }

    @PostMapping(path = {"/addPlayer"})
    public ResponseEntity<SimpleUser> addPlayer(@RequestBody SimpleUser player) {
        if (isNullOrEmpty(player.getName())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(player);
        SimpleUser savedPlayer = leaderboardRepository.save(player);
        return ResponseEntity.ok(savedPlayer);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

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
