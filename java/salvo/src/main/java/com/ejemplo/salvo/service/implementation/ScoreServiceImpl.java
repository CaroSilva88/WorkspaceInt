package com.ejemplo.salvo.service.implementation;

import com.ejemplo.salvo.model.Score;
import com.ejemplo.salvo.repository.ScoreRepository;
import com.ejemplo.salvo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    ScoreRepository scoreRepository;

    @Override
    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }

    @Override
    public List<Score> getScore() {
        return null;
    }

    @Override
    public Score updateScore(Score score) {
        return null;
    }

    @Override
    public boolean deleteScore(Long id) {
        return false;
    }

    @Override
    public Score findScoreById(Long id) {
        return scoreRepository.findById(id).get();
    }
}
