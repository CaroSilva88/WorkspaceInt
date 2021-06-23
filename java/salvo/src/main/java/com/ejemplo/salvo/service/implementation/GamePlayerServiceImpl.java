package com.ejemplo.salvo.service.implementation;

import com.ejemplo.salvo.model.GamePlayer;
import com.ejemplo.salvo.repository.GamePlayerRepository;
import com.ejemplo.salvo.service.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlayerServiceImpl implements GamePlayerService {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Override
    public GamePlayer saveGamePlayer(GamePlayer gamePlayer) {
        return gamePlayerRepository.save(gamePlayer);
    }

    @Override
    public List<GamePlayer> getGamePlayer() {
        return null;
    }

    @Override
    public GamePlayer updateGamePLayer(GamePlayer gamePlayer) {
        return null;
    }

    @Override
    public boolean deleteGamePlayer(Long id) {
        return false;
    }

    @Override
    public GamePlayer finGamePlayerById(Long id) {
        return gamePlayerRepository.findById(id).get();
    }
}
