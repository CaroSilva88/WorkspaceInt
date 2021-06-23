package com.ejemplo.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idPlayer;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scoreSet;

    private String userName;
    private String password;

    //constructores
    public Player() {
    }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    ///////////////////
    //getters y setters
    public long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    ///////////
    //to String
    @Override
    public String toString() {
        return "Player{" +
                "idPlayer=" + idPlayer +
                ", userName='" + userName + '\'' +
                '}';
    }


    //////metodo auxiliar para mostrar puntuaciones////////
    /*public Score getScore(Game game){

        Optional<Score> optionalScore = getScoreSet().stream().findFirst();
        if (optionalScore.isPresent()) {
            return optionalScore.get() ;
        } else {
            return null;
        }

    }*/

}
