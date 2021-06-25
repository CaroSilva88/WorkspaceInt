package com.ejemplo.salvo.service;

import com.ejemplo.salvo.model.Salvo;
import com.ejemplo.salvo.model.Score;

import java.util.List;

public interface ScoreService {

    Score saveScore(Score score);

    List<Score> getScore();

    Score updateScore(Score score);

    boolean deleteScore(Long id);

    Score findScoreById(Long id);
}
