package com.ejemplo.salvo.controller;

import com.ejemplo.salvo.model.*;
import com.ejemplo.salvo.repository.GameRepository;
import com.ejemplo.salvo.repository.PlayerRepository;
import com.ejemplo.salvo.repository.SalvoRepository;
import com.ejemplo.salvo.repository.ShipRepository;
import com.ejemplo.salvo.service.GamePlayerService;
import com.ejemplo.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    /*******Inyeccion de dependencias******/

    @Autowired
    private GameRepository gameRepo;

    //@Autowired
    //private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GamePlayerService gamePlayerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    //@Autowired
    //private PlayerService playerService;

    @Autowired
    private SalvoRepository salvoRepository;


    /////////////////////////////////////////

    /****************************************/


    @RequestMapping("/games")
    public Map<String, Object> gamesList(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        //retorno utilizando el repositoio a traves de la inyeccion de dependencias, mostrare una lista, iterandola con el metodo
        //stream y utilizando el metodo map como apoyo en esta iteracion, con ello llamo al metodo makeGameDTO en el cual
        //indico los atributos que quiero mostrar en dicha lista
        dto.put("player", !Util.isGuest(authentication) ? makePlayerDTO(playerRepository.findByUserName(authentication.getName())) : "Guest");
        dto.put("games", gameRepo.findAll().stream().map(games -> this.makeGameDTO(games)).collect(Collectors.toList()));
        return dto;

    }

    /*************************************************************************************************************/

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path = "/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * En este metodo puedo crear un juego, estando ya logueado.
     * si el usuario es un invitado, le envio un error ya que no esta autorizado a crear un nuevo juego.
     * en el caso contrario, se crea un nuevo juego, y llamo al player que esta logueado y ya con esos dos
     * objetos puedo crear un nuevo gamePlayer y por lo tanto se crea el id correspondiente
     **/
    @PostMapping(path = "/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {


        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "usuario no autorizado"), HttpStatus.UNAUTHORIZED);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            Game game = gameRepo.save(new Game(new Date()));
            GamePlayer gamePlayer =  gamePlayerService.saveGamePlayer(new GamePlayer(game, player));
            // GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getIdGP()), (HttpStatus.CREATED));
        }
    }

    @GetMapping("/game_view/{id}")
    public ResponseEntity<Map<String, Object>> getGame_view(@PathVariable long id, Authentication authentication) {

        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "usuario no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());

        GamePlayer gamePlayer = gamePlayerService.finGamePlayerById(id);

        if (gamePlayer == null) {
            return new ResponseEntity<>(Util.makeMap("error", "esta partida no existe"), HttpStatus.FORBIDDEN);
        }

        /*Si el "idPlayer" del player que esta en una partida es distinto al "idPlayer" del player en la misma partida,
         * se lanzara un error de usuario no autorizado, en el caso de que el player quisiera ver lo que esta haciendo
         * el otro player, en el mismo juego*/
        if (gamePlayer.getPlayer().getIdPlayer() != player.getIdPlayer()) {
            return new ResponseEntity<>(Util.makeMap("error", "usuario no autorizado"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(this.makeGame_viewDTO(gamePlayer), HttpStatus.ACCEPTED);

    }


    @PostMapping(path = "/game/{idGame}/players")
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication,
                                                        @PathVariable long idGame) {


        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());

        Game game = gameRepo.findById(idGame).get();

        if (game == null) {

            return new ResponseEntity<>(Util.makeMap("error", "Este juego no existe"), HttpStatus.FORBIDDEN);
        }

        if (player == null) {

            return new ResponseEntity<>(Util.makeMap("error", "este jugador no existe"), HttpStatus.FORBIDDEN);
        }


        if (game.getGamePlayerSet().stream().filter(gamePlayer -> gamePlayer.getPlayer() == player).collect(Collectors.toList()).contains(player))
                                    {
            return new ResponseEntity<>(Util.makeMap("error", "No puedes unirte dos veces al mismo juego"), HttpStatus.FORBIDDEN);
        }

        if (game.getGamePlayerSet().size() == 1) {
            GamePlayer gameP = gamePlayerService.saveGamePlayer(new GamePlayer(game, player));
            return new ResponseEntity<>(Util.makeMap("gpid", gameP.getIdGP()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Util.makeMap("error", "La partida esta llena"), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping(path = "/games/players/{gpid}/ships")
    public ResponseEntity<Map> addShips(Authentication authentication, @PathVariable long gpid, @RequestBody Set<Ship> ships) {


        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gamePlayerService.finGamePlayerById(gpid);

        if (gamePlayer == null) {

            return new ResponseEntity<>(Util.makeMap("error", "Este juego no existe"), HttpStatus.UNAUTHORIZED);
        }

        if (player == null) {

            return new ResponseEntity<>(Util.makeMap("error", "este jugador no existe"), HttpStatus.UNAUTHORIZED);
        }

        if (!gamePlayer.getShips().isEmpty()) { //isEmpty() devuelve true si este conjunto no contiene elementos
            return new ResponseEntity<>(Util.makeMap("error", "ya escogiste barcos"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getIdPlayer() != player.getIdPlayer()) {
            return new ResponseEntity<>(Util.makeMap("error", "Este no es tu juego"), HttpStatus.UNAUTHORIZED);
        }



        /* if (ship.getCells().listIterator() == null) {
                return new ResponseEntity<>(Util.makeMap("error", "Escoge tus barcos"), HttpStatus.UNAUTHORIZED);
            }

            if (ship.getShipType() == "Aircraft Carrier" && ship.getCells().stream().count() != 5){
                return new ResponseEntity<>(Util.makeMap("Error", "Ingresa un numero de celdas Valido"), HttpStatus.FORBIDDEN);
            }

            if (ship.getShipType() == "Battleship" && ship.getCells().stream().count() != 4){
                return new ResponseEntity<>(Util.makeMap("Error", "Ingresa un numero de celdas Valido"), HttpStatus.FORBIDDEN);
            }

            if (ship.getShipType() == "Submarine" && ship.getCells().stream().count() != 3){
                return new ResponseEntity<>(Util.makeMap("Error", "Ingresa un numero de celdas Valido"), HttpStatus.FORBIDDEN);
            }

            if (ship.getShipType() == "Destroyer" && ship.getCells().stream().count() != 3){
                return new ResponseEntity<>(Util.makeMap("Error", "Ingresa un numero de celdas Valido"), HttpStatus.FORBIDDEN);
            }

            if (ship.getShipType() == "Patrol Boat" && ship.getCells().stream().count() != 2){
                return new ResponseEntity<>(Util.makeMap("Error", "Ingresa un numero de celdas Valido"), HttpStatus.FORBIDDEN);
            }*/


        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        });

        return new ResponseEntity<>(Util.makeMap("OK", "Ships created"), HttpStatus.CREATED);
    }

    @PostMapping(path = "/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> createSalvos(Authentication authentication,
                                                            @PathVariable long gamePlayerId,
                                                            @RequestBody Salvo salvo) {


        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gameP = gamePlayerService.finGamePlayerById(gamePlayerId);
        Optional<GamePlayer> player2 = gameP.getGame().getGamePlayerSet().stream().filter(gp -> gp.getPlayer()
                                    != gameP.getPlayer()).findFirst();

        //game player no existe
        if (gameP == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Este juego no existe"), HttpStatus.UNAUTHORIZED);
        }

        //player no existe
        if (player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "este jugador no existe"), HttpStatus.UNAUTHORIZED);
        }

        //el player que esta logueado debe ser el mismo que muestra la url
        if (gameP.getPlayer().getIdPlayer() != player.getIdPlayer()) {
            return new ResponseEntity<>(Util.makeMap("error", "Este no es tu juego"), HttpStatus.UNAUTHORIZED);
        }

        if (!player2.isPresent()){
            return new ResponseEntity<>(Util.makeMap("error", "Espera a tu oponente"), HttpStatus.FORBIDDEN);
        }

        //validar que la cantidad de tiros sea de 1 a 5
        int hits = salvo.getSalvoLocations().size();
        if (hits <= 1 && hits >= 5) {
            return new ResponseEntity<>(Util.makeMap("error", "Pedes lanzar entre 1 y 5 tiros"), HttpStatus.FORBIDDEN);
        }

        //Se debe enviar una respuesta prohibida si el usuario ya ha enviado una salva para el turno indicado y
        // desea lanzar una salva en el siguiente turno.
        int playerTurn = gameP.getSalvos().stream().mapToInt(sal -> sal.getTurn()).max().orElse(0);
        System.out.println("PlayerTurn:" + playerTurn);
        int opponentTurn = player2.get().getSalvos().stream().mapToInt(s -> s.getTurn()).max().orElse(0);
        System.out.println("opponentTurn: " + opponentTurn);
        if (playerTurn > opponentTurn){
           return new ResponseEntity<>(Util.makeMap("error", "Espera tu turno"), HttpStatus.FORBIDDEN);
        }

        //contador para el turno
        int turno = 0;
        if (gameP.getSalvos().size() == 0) {
            turno = 1;
        } else {
            turno = gameP.getSalvos().size()  + 1;
        }

        System.out.println("turno: " + turno);

       salvo.setGamePlayer(gameP);
       salvo.setTurn(turno);
       salvoRepository.save(salvo);
        return new ResponseEntity<>(Util.makeMap("OK", "Salvos created"), HttpStatus.CREATED);
    }


/***********************************************************************************************************/
    ///////////////////////////////////////////////////////

    /****Metodos que sobrecargan el metodo gamesList****/
    public Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getIdGame());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayerSet().stream().map(gamePlayer -> makeGamePlayerDTO(gamePlayer)).collect(Collectors.toList()));
        dto.put("scores", game.getScoreSet().stream().map(score -> makeScoreDTO(score)).collect(Collectors.toList()));
        return dto;
    }

    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayers) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", (gamePlayers.getIdGP()));
        dto.put("player", this.makePlayerDTO(gamePlayers.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getIdPlayer());
        dto.put("email", player.getUserName());

        return dto;
    }
    /////////////////////////////////////////////////////

    /****Metodos que sobrecargan el metodo controlador game_view****/
    private Map<String, Object> makeGame_viewDTO(GamePlayer gameP) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Map<String, Object> hits = new LinkedHashMap<String, Object>();

        if (gameP.getGame().getGamePlayerSet().size() == 2){
            hits.put("self", this.getHitsDTO(gameP, this.getOpponent(gameP) ));
            hits.put("opponent", this.getHitsDTO(this.getOpponent(gameP), gameP));
        }else {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }

        dto.put("id", gameP.getGame().getIdGame());
        dto.put("created", gameP.getGame().getCreationDate());
        //dto.put("gameState", this.getState(gameP));

        dto.put("gamePlayers", gameP.getGame().getGamePlayerSet()
                                              .stream()
                                              .map(this::makeGamePlayerDTO)
                                              .collect(Collectors.toList()));
        dto.put("ships", gameP.getShips()
                              .stream()
                              .map(ship -> makeShipDTO(ship))
                              .collect(Collectors.toList()));
        dto.put("salvoes", gameP.getGame().getGamePlayerSet()
                                          .stream()
                                          .flatMap(gp -> gp.getSalvos()
                                                           .stream()
                                                           .map(salvo -> makeSalvoDTO(salvo)))
                                                           .collect(Collectors.toList()));

        dto.put("hits", hits);



        return dto;
    }

    /*private String getState(GamePlayer gamePlayer) {

        if (gamePlayer.getShips().isEmpty()){
            return "PLACESHIPS";
        }


        if(gamePlayer.getGame().getGamePlayerSet().size() == 1 || this.getOpponent(gamePlayer).getShips().isEmpty()){
            return "WAIT";
        }

        if (gamePlayer.getSalvos().size() > this.getOpponent(gamePlayer).getSalvos().size()){
            return "WAITINGFOROPP";
        }


        if (this.getLost(gamePlayer, this.getOpponent(gamePlayer))){
            return "LOST";
        }

        if (this.getWin(gamePlayer, this.getOpponent(gamePlayer))){
            return "WIN";
        }

        if (this.getLost(gamePlayer, this.getOpponent(gamePlayer)) && this.getWin(gamePlayer, this.getOpponent(gamePlayer)) ){
            return "TIE";
        }
        return "PLAY";
    }


    private Boolean getLost (GamePlayer self, GamePlayer opponent){
        if (!self.getSalvos().isEmpty() && !opponent.getSalvos().isEmpty()){
            //retorna true si la lista de salvoLocations del oponente contine todas las ShipLocations de self
            return opponent.getSalvos().stream()
                    .flatMap(salvo -> salvo.getSalvoLocations()
                            .stream())
                    .collect(Collectors.toList())
                    .containsAll(self.getShips().stream()
                            .flatMap(ship -> ship.getShipLocations().stream())
                            .collect(Collectors.toList()));
        }
        return false;
    }

    private Boolean getWin (GamePlayer self, GamePlayer opponent){
        if (!self.getSalvos().isEmpty() && !opponent.getSalvos().isEmpty()){
            //retorna true si la lista de salvoLocations de self  contine todas las ShipLocations del oponente
            return self.getSalvos().stream()
                    .flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList())
                    .containsAll(opponent.getShips().stream()
                            .flatMap(ship -> ship.getShipLocations().stream())
                            .collect(Collectors.toList()));
        }
        return false;
    }*/

    /*Metodo para obtener al oponente*/
    private GamePlayer getOpponent(GamePlayer opponent){
        return opponent.getGame().getGamePlayerSet()
                                 .stream()
                                 .filter(gp -> (gp.getIdGP() != opponent.getIdGP()))
                                 .findFirst()
                                 .orElse(new GamePlayer());

    }

    private Map<String, Object> makeShipDTO(Ship ships) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ships.getType());
        dto.put("locations", ships.getShipLocations());
        return dto;
    }
    /////////////////////////////////////////////////////////////////////

    /***********Metodo que sobrecarga a game_viewDTO con los salvos****/

    private Map<String, Object> makeSalvoDTO(Salvo salvos) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvos.getTurn());
        dto.put("player", salvos.getGamePlayer().getPlayer().getIdPlayer());
        dto.put("locations", salvos.getSalvoLocations());
        return dto;
    }
//////////////////////////////////////////////////////////////////////////////////
    /*Ultima Parte*/

    //los hits se basan en iterar los salvos
    private List<Map<String, Object>> getHitsDTO(GamePlayer self, GamePlayer opponent){
        List<Map<String, Object>> hits = new ArrayList<>(); //creo la lista de mapas para hits
        /*contadores del juego*/
        int carrierDamage = 0;
        int battleshipDamage = 0;
        int submarineDamage = 0;
        int destroyerDamage = 0;
        int patrolsDamage = 0;

        /*List<String> carrierLocations = getHitsLocations("carrier", self);
        List<String> battleshipLocations = getHitsLocations("battleship", self);
        List<String> submarineLocations = getHitsLocations("submarine", self);
        List<String> destroyerLocations = getHitsLocations("destroyer", self);
        List<String> patrolboatLocations = getHitsLocations("patrolboat", self);*/

        for (Salvo salvo: opponent.getSalvos()) {

        }

        //itero los salvos
        for (Salvo salvo : opponent.getSalvos()) {
            /*contadores por turno*/
            int carrierHits = 0;
            int battleshipHits = 0;
            int submarineHits = 0;
            int destroyerHits = 0;
            int patrolsHits = 0;

            int missed = salvo.getSalvoLocations().size();

            Map<String, Object> damagesPorTur = new LinkedHashMap<String, Object>();
            Map<String, Object> hitsPorTurn = new LinkedHashMap<String, Object>();

            List<String> hitsLocations = new ArrayList<>();
            List<String> salvoLocationList = new ArrayList<>();

            //por cada salvo debo iterar los tiros
            for (String locations : salvo.getSalvoLocations()) {

                if ( this.getHitsLocations("carrier", self).contains(locations) /*carrierLocations.contains(locations)*/){

                    carrierHits++;
                    carrierDamage++;
                    hitsLocations.add(locations);
                    missed--;
                }
                if (this.getHitsLocations("battleship", self).contains(locations)/*battleshipLocations.contains(locations)*/){

                    battleshipHits++;
                    battleshipDamage++;
                    hitsLocations.add(locations);
                    missed--;
                }
                if (this.getHitsLocations("submarine", self).contains(locations)/*submarineLocations.contains(locations)*/){

                    submarineDamage++;
                    submarineHits++;
                    hitsLocations.add(locations);
                    missed--;
                }
                if (this.getHitsLocations("destroyer", self).contains(locations)/*destroyerLocations.contains(locations)*/){
                    destroyerHits++;
                    destroyerDamage++;
                    hitsLocations.add(locations);
                    missed--;
                }
                if (this.getHitsLocations("patrolboat", self).contains(locations)/*patrolboatLocations.contains(locations)*/){
                    patrolsHits++;
                    patrolsDamage++;
                    hitsLocations.add(locations);
                    missed--;
                }

                damagesPorTur.put("carrierHits", carrierHits);
                damagesPorTur.put("battleshipHits", battleshipHits);
                damagesPorTur.put("submarineHits", submarineHits);
                damagesPorTur.put("destroyerHits", destroyerHits);
                damagesPorTur.put("patrolboatHits", patrolsHits);
            }



            damagesPorTur.put("carrier", carrierDamage);
            damagesPorTur.put("battleship", battleshipDamage);
            damagesPorTur.put("submarine", submarineDamage);
            damagesPorTur.put("destroyer", destroyerDamage);
            damagesPorTur.put("patrolboat", patrolsDamage);

            hitsPorTurn.put("turn", salvo.getTurn());
            hitsPorTurn.put("hitLocations", hitsLocations);
            hitsPorTurn.put("damagesPorTur", damagesPorTur);
            hitsPorTurn.put("missed", missed);
            hits.add(hitsPorTurn);
        }

        return hits;
    }

    private List<String> getHitsLocations(String type, GamePlayer self){
        /*List<String> locations = new ArrayList<>();
        Optional<Ship> ship = self.getShips()
                                  .stream()
                                  .filter(sh -> sh.getType().equals(type))
                                  .findFirst();
        if (ship.isPresent()){
            locations = ship.get().getShipLocations();
        }
        return locations;*/

        GamePlayer gamePlayer = gamePlayerService.finGamePlayerById(self.getIdGP());
        List<String> locations = gamePlayer.getShips()
                                           .stream()
                                           .filter(ship -> ship.getType().equals(type))
                                           .map(Ship::getShipLocations)
                                           .flatMap(Collection::stream)
                                           .collect(Collectors.toList());

        return locations;
    }



    //////////////////////////////////////////////////////////////////////

    /*********Metodo que entrega info de Score**************************/
    private Map<String, Object> makeScoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("score", score.getScore());
        dto.put("player", score.getPlayer().getIdPlayer());
        return dto;

    }
}
