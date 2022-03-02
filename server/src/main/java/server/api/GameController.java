package server.api;


import commons.GameInstance;
import commons.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final ActivityRepository activityRepository;

    private final Random random;
    private final List<GameInstance> gameInstances;
    private final List<Player> players;

    public GameController(Random random, ActivityRepository activityRepository){
        this.random = random;
        this.activityRepository = activityRepository;
        System.out.println(this.activityRepository.findAll());
        gameInstances = new ArrayList<>();
        gameInstances.add(new GameInstance(gameInstances.size(), GameInstance.MULTI_PLAYER));

        players = new ArrayList<>();
    }

    @PostMapping("/join")
    public ResponseEntity<Player> addPlayer(@RequestBody String name){
        if(isNullOrEmpty(name)) return ResponseEntity.badRequest().build();
        Player savedPlayer = new Player(players.size(), gameInstances.get(gameInstances.size() - 1), name);
        players.add(savedPlayer);
        gameInstances.get(gameInstances.size() - 1).players.add(savedPlayer);

        return ResponseEntity.ok(savedPlayer);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


}
