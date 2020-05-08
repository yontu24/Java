package com.example.demo.service;

import com.example.demo.dao.PlayerDao;
import com.example.demo.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerDao playerDao;

    @Autowired
    public PlayerService(@Qualifier("fakePlayer") PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public int addPlayer(int id, Player p) {
        return playerDao.insertPlayer(id, p);
    }

    public List<Player> getPlayers() {
        return playerDao.getAll();
    }

    public Optional<Player> getPlayerById(int id) {
        return playerDao.selectPlayerById(id);
    }

    public int deletePlayer(int id) {
        return playerDao.deletePlayerById(id);
    }

    public int updatePlayer(int id, Player player) {
        return playerDao.updatePlayerById(id, player);
    }
}
