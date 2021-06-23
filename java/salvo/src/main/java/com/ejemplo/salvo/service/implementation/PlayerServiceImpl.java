package com.ejemplo.salvo.service.implementation;

import com.ejemplo.salvo.model.Player;
import com.ejemplo.salvo.repository.PlayerRepository;
import com.ejemplo.salvo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> getPlayer() {
        return null;
    }

    @Override
    public Player updatePLayer(Player player) {
        return null;
    }

    @Override
    public boolean deletePlayer(Long id) {
        return false;
    }

    @Override
    public Player finPlayerById(Long id) {
        return null;
    }



}
