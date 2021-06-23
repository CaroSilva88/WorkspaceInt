package com.ejemplo.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idGame;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scoreSet;

    private Date creationDate;

    /////////constructores///////
    public Game() {
    }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }


    ////////getters y setters/////////


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getIdGame() {

        return idGame;
    }

    public void setIdGame(long idGame) {
        this.idGame = idGame;
    }

    //-
    @JsonIgnore
    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }

    public Set<Score> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<Score> scoreSet) {
        this.scoreSet = scoreSet;
    }

    /////toString////////
    @Override
    public String toString() {
        return "Game{" +
                "idGame=" + idGame +
                ", creationDate=" + creationDate +
                '}';
    }

}
