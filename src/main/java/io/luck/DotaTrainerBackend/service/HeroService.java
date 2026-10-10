package io.luck.DotaTrainerBackend.service;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.repository.HeroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class HeroService {

    private final HeroRepository heroRepository;

    public HeroService(HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    // TODO get hero from cache (seed cache if empty)
    public Hero getHeroById(Long id){
        return heroRepository.findById(id).orElse(null);
    }

    public Page<Hero> getAllPaged(Pageable pageable){
        return heroRepository.findAll(pageable);
    }

    public List<Hero> getAll(){
        return heroRepository.findAll();
    }

    public List<Hero> getAllById(Set<Long> ids){
        return heroRepository.findAllById(ids);
    }

}
