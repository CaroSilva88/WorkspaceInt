package com.ejemplo.salvo.service.implementation;

import com.ejemplo.salvo.model.Ship;
import com.ejemplo.salvo.repository.ShipRepository;
import com.ejemplo.salvo.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    ShipRepository shipRepository;

    @Override
    public Ship saveShip(Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public List<Ship> getShip() {
        return null;
    }

    @Override
    public Ship updateShip(Ship ship) {
        return null;
    }

    @Override
    public boolean deleteShip(Long id) {
        return false;
    }

    @Override
    public Ship findShipById(Long id) {
        return shipRepository.findById(id).get();
    }
}
