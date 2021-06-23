package com.ejemplo.salvo.repository;

import com.ejemplo.salvo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {

     //@Autowired
     //GameRepository playerRepository = null;
}
