package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.domain.Hero;
import com.br.app.DotaTrainerBackend.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class HeroService extends BaseService{

    @Autowired
    private HeroRepository heroRepository;

    // TODO get hero from cache (seed cache if empty)
    public Hero getHeroById(Integer id){
        return heroRepository.findById(id).orElse(null);
    }

    public Page<Hero> getAllPaged(Pageable pageable){
        return heroRepository.findAll(pageable);
    }

    public List<Hero> getAll(){
        return heroRepository.findAll();
    }

    public List<Hero> getAllById(Set<Integer> ids){
        return heroRepository.findAllById(ids);
    }

}
