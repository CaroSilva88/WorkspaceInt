package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.Game;
import com.ejemplo.salvo.model.Player;

import java.util.List;


public interface GameService {

    Game saveGame(Game game);

    List<Game> getGame();

    Game updateGame(Game game);

    boolean deleteGame (Long id);

    Game finGameById(Long id);
}
