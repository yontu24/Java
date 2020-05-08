package com.example.demo.dao;
import com.example.demo.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {

    int insertPlayer(int id, Player player);

    List<Player> getAll();

    Optional<Player> selectPlayerById(int id);

    int deletePlayerById(int id);

    int updatePlayerById(int id, Player player);
}
