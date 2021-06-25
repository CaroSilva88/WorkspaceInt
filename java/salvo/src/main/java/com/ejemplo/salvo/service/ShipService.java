package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.Score;
import com.ejemplo.salvo.model.Ship;

import java.util.List;

public interface ShipService {

    Ship saveShip(Ship ship);

    List<Ship> getShip();

    Ship updateShip(Ship ship);

    boolean deleteShip(Long id);

    Ship findShipById(Long id);
}
