package com.br.app.DotaTrainerBackend.resource;

import com.br.app.DotaTrainerBackend.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.br.app.DotaTrainerBackend.model.Hero;

import java.util.List;

@RestController
@RequestMapping("/heroes")
public class HeroResource {

    @Autowired
    private HeroRepository heroRepository;

    public List<Hero> getAll(){
        return this.heroRepository.findAll();
    }
}
