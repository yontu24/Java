package com.example.demo.api;

import com.example.demo.model.Player;
import com.example.demo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("api\\player")
@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public void addPlayer(@RequestBody int id, @RequestBody Player player){
        playerService.addPlayer(id, player);
    }

    @GetMapping
    public List<Player> getPlayers() {
        return playerService.getPlayers();
    }

    @GetMapping(path = "{id}")
    public Player getPlayerById(@PathVariable("id") int id) {
        return playerService.getPlayerById(id).orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deletePlayerById(@PathVariable("id") int id) {
        playerService.deletePlayer(id);
    }

    @PutMapping
    public void updatePlayer(@PathVariable("id") int id, @Valid @NotNull @RequestBody Player player) {
        playerService.updatePlayer(id, player);
    }
}
