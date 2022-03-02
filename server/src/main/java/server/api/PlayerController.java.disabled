package server.api;

import commons.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final Random random;

    private final PlayerRepository playerRepository;

    public PlayerController(Random random, PlayerRepository playerRepository) {
        this.random = random;
        this.playerRepository = playerRepository;
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        if (isNullOrEmpty(player.name)) {
            return ResponseEntity.badRequest().build();
        }
        //playerRepository.save(player);
        Player savedPlayer = playerRepository.save(new Player(player.name));
        return ResponseEntity.ok(savedPlayer);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") long id, @RequestBody Player player) {
        Optional<Player> playerData = playerRepository.findById(id);
        if (playerData.isPresent()) {
            Player newPlayer = playerData.get();
            if (!isNullOrEmpty(player.name)) newPlayer.name = player.name;
            newPlayer.score = 0;
            newPlayer.status = 0;
            newPlayer.powerUpUsed = new ArrayList<>();
            newPlayer.id = id;
            return ResponseEntity.ok(playerRepository.save(newPlayer));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") long id) {
        if (playerRepository.existsById(id)) {
            try {
                playerRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/all")
    public ResponseEntity<Player> deleteAll() {
        try {
            playerRepository.deleteAll();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
