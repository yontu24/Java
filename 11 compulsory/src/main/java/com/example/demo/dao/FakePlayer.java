package com.example.demo.dao;

import com.example.demo.model.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fakePlayer")
public class FakePlayer implements PlayerDao {

    private static List<Player> database = new ArrayList<>();
    @Override
    public int insertPlayer(int id, Player player) {
        database.add(new Player(id, player.getName()));
        return 1;
    }

    @Override
    public List<Player> getAll() {
        return database;
    }

    @Override
    public Optional<Player> selectPlayerById(int id) {
        return database.stream()
                .filter(player -> player.getId() == id)
                .findFirst();
    }

    @Override
    public int deletePlayerById(int id) {
        Optional<Player> player = selectPlayerById(id);
        if(player.isPresent()) {
            database.remove(player.get());
            return 1;
        }
        return 0;
    }

    @Override
    public int updatePlayerById(int id, Player player) {
        return selectPlayerById(id).map(p -> {
            int idPlayer = database.indexOf(p);
            if (idPlayer >= 0){
                database.set(idPlayer, new Player(id, p.getName()));
                return 1;
            }
            return 0;
        }).orElse(0);
    }
}
