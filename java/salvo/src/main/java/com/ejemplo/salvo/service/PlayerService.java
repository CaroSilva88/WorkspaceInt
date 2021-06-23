package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.Player;

import java.util.List;

public interface PlayerService {

    Player savePlayer(Player player);

    List<Player> getPlayer();

    Player updatePLayer(Player player);

    boolean deletePlayer (Long id);

    Player finPlayerById(Long id);

}
