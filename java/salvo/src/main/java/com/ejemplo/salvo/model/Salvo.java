package com.ejemplo.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idSalvo;
    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer")
    private GamePlayer gamePlayer;

    @ElementCollection
    @JoinColumn(name = "cellSalvo")
    private List<String> salvoLocations = new ArrayList<>();

    //////////////////
    //Constructores//


    public Salvo() {
    }

    public Salvo(int turn, GamePlayer gamePlayer, List<String> salvoLocations) {
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
    }

    //////////////////////
    //Getters y Setters//


    public long getIdSalvo() {
        return idSalvo;
    }

    public void setIdSalvo(long idSalvo) {
        this.idSalvo = idSalvo;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    ////////////////////
    ///toString////


    @Override
    public String toString() {
        return "Salvo{" +
                "idShip=" + idSalvo +
                ", turn=" + turn +
                ", gamePlayer=" + gamePlayer +
                ", cellsSalvo=" + salvoLocations +
                '}';
    }
}
