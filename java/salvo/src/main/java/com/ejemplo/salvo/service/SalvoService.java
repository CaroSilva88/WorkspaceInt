package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.Player;
import com.ejemplo.salvo.model.Salvo;

import java.util.List;

public interface SalvoService {

    Salvo saveSalvo(Salvo salvo);

    List<Salvo> getSalvo();

    Salvo updateSalvo(Salvo salvo);

    boolean deleteSalvo (Long id);

    Salvo findSalvoById(Long id);
}
