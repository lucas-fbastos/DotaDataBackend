package io.luck.DotaTrainerBackend.resource;

import io.luck.DotaTrainerBackend.model.entity.Hero;
import io.luck.DotaTrainerBackend.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/heroes")
public class HeroResource {

    @Autowired
    private HeroService heroService;

    @GetMapping
    public ResponseEntity<List<Hero>> getAll(){
        List<Hero> heroes = heroService.getAll();
        if(!heroes.isEmpty())
            return ResponseEntity.ok(heroes);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hero> getById(@PathVariable Integer id){
        Hero hero = heroService.getHeroById(id);
        if(hero!=null)
            return ResponseEntity.ok(hero);
        else
            return ResponseEntity.notFound().build();
    }
}
