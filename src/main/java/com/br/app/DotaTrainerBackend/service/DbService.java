package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.Hero;
import com.br.app.DotaTrainerBackend.model.Role;
import com.br.app.DotaTrainerBackend.repository.HeroRepository;
import com.br.app.DotaTrainerBackend.repository.RoleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class DbService extends BaseService{

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String[] ROLES = {"Carry","Nuker","Initiator","Disabler","Support","Escape","Durable","Pusher"};

    public void instantiateTestDatabase(){
        seedHeroRoles();
        saveHeroesFromApi();
    }

    private void seedHeroRoles(){ for(String role : ROLES) roleRepository.save(new Role(role));}

    private void saveHeroesFromApi(){
        String heroesFromAPI = this.getFromApi(this.apiUrl+"constants/heroes");
        JSONObject heroesObj = new JSONObject(heroesFromAPI);
        Set<String> keys = heroesObj.keySet();
        for( String key : keys){
            JSONObject jsonHero = heroesObj.getJSONObject(String.valueOf(key));
            Hero hero = new Hero(jsonHero);
            JSONArray rolesJson = jsonHero.getJSONArray("roles");
            List<String > rolesList = rolesJson.toList().stream().map(Object::toString).toList();
            List<Role> roles = roleRepository.findByDescriptionIn(rolesList);
            hero.setRoles(roles);
            heroRepository.save(hero);
        }
    }
}
