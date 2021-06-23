package com.ejemplo.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idGame")
    private Game game;


    @ManyToOne(fetch = FetchType.EAGER)//esta linea me entrega o selecciona todas las relaciones que hay en esta clase
    @JoinColumn(name="idPlayer")
    private Player player;

    private double score;

    private Date finishDate;

    ////////////////
    //Constructores//


    public Score() {
    }

    public Score(Game game, Player player, double score) {
        this.game = game;
        this.player = player;
        this.score = score;
        //this.finishDate = finishDate;
    }

    ///////////////////
    //Getters y Setters//


    public long getIdScore() {
        return idScore;
    }

    public void setIdScore(long idScore) {
        this.idScore = idScore;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    ////////////////////
    ///toString///


    @Override
    public String toString() {
        return "Score{" +
                "idScore=" + idScore +
                ", game=" + game +
                ", player=" + player +
                ", score=" + score +
                ", finishDate=" + finishDate +
                '}';
    }
}
