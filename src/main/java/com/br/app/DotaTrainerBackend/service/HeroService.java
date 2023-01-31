package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.Hero;
import com.br.app.DotaTrainerBackend.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeroService extends BaseService{

    @Autowired
    private HeroRepository heroRepository;

    // TODO get hero from cache (seed cache if empty)
    public Hero getHeroById(Integer id){
        return heroRepository.findById(id).orElse(null);
    }
}
