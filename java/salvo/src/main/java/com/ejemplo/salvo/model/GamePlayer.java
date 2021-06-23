package com.ejemplo.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long idGP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idGame")
    private Game game;


    @ManyToOne(fetch = FetchType.EAGER)//esta linea me entrega o selecciona todas las relaciones que hay en esta clase
    @JoinColumn(name="idPlayer")
    private Player player;

    private LocalDate joinDate;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships= new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    @OrderBy
    Set<Salvo> salvos= new HashSet<>();

    //constructores

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    //getters y setters

    public void setIdGP(long idGP) {
        this.idGP = idGP;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getIdGP() {
        return idGP;
    }

    public void setId(int idGP) {
        this.idGP = idGP;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {

        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    /***????***/
    public  void addShip(Ship addShip){
        //addShip.setIdGP(this);
        this.ships.add(addShip);
    }
    ///////////

    //to String//
    @Override
    public String toString() {
        return "GamePlayer{" +
                "idGP=" + idGP +
                ", gameID=" + game +
                ", playerID=" + player +
                ", joinDate=" + joinDate +
                '}';
    }

    ///////////////////////////////////////////
   /* public Score getScore(){

        Optional<Score> optionalScore = getScore().getPlayer().getScoreSet().stream().findFirst();
        if (optionalScore.isPresent()) {
            return optionalScore.get() ;
        } else {
            return null;
        }

    }*/


}
