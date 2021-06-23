package com.ejemplo.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idShip;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer")
    private GamePlayer gamePlayer;

    @ElementCollection
    @JoinColumn(name = "cell")
    private List<String> shipLocations = new ArrayList<>();


    ///constructores///

    public Ship() {
    }

    public Ship(String type, GamePlayer gamePlayer, List<String> shipLocations) {

        this.type = type;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }
    ///////////////////////
    ///Getters y Setters///

    public long getIdShip() {
        return idShip;
    }

    public void setIdShip(long idShip) {
        this.idShip = idShip;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //////////////
    ///ToString///

    @Override
    public String toString() {
        return "Ship{" +
                "idShip=" + idShip +
                ", player=" + gamePlayer +
                ", cells=" + shipLocations +
                ", shipType='" + type + '\'' +
                '}';
    }
}
