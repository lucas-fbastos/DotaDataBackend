package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.entity.Hero;
import io.luck.AghsLabs.service.HeroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/heroes")
public class HeroResource {

    private final HeroService heroService;

    public HeroResource(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping
    public ResponseEntity<List<Hero>> getAll(){
        List<Hero> heroes = heroService.getAll();
        if(!heroes.isEmpty())
            return ResponseEntity.ok(heroes);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hero> getById(@PathVariable Long id){
        Hero hero = heroService.getHeroById(id);
        if(hero!=null)
            return ResponseEntity.ok(hero);
        else
            return ResponseEntity.notFound().build();
    }
}
