package com.ejemplo.salvo.repository;

import com.ejemplo.salvo.model.GamePlayer;
import com.ejemplo.salvo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {



   Player findByUserName(@Param("userName") String userName);

   // @Autowired
   // PlayerRepository playerRepository = null;
}
