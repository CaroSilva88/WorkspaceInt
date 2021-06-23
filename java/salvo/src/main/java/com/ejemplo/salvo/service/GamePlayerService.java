package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.GamePlayer;

import java.util.List;

/*
Como dice la definicion de interfaz, no puedo desarrollar los metodos dentro de ella, si no,
solo la utilizo para definir los metodos que se implementaran en otra clase
 */
public interface GamePlayerService {

    GamePlayer saveGamePlayer(GamePlayer gamePlayer);

    List<GamePlayer> getGamePlayer();

    GamePlayer updateGamePLayer(GamePlayer gamePlayer);

    boolean deleteGamePlayer (Long id);

    GamePlayer finGamePlayerById(Long id);


}
