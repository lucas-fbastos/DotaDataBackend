package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.Hero;
import com.br.app.DotaTrainerBackend.model.Role;
import com.br.app.DotaTrainerBackend.repository.HeroRepository;
import com.br.app.DotaTrainerBackend.repository.RoleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DbService {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String[] ROLES = {"Carry","Nuker","Initiator","Disabler","Support","Escape","Durable","Pusher"};

    public void instantiateTestDatabase(){
        seedHeroRoles();
        getHeroesFromApi();
    }

    private void seedHeroRoles(){
        for (String role : ROLES) roleRepository.save(new Role(role));
    }

    private void getHeroesFromApi(){

        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://api.opendota.com/api/constants/heroes";
        String jsonString = restTemplate.getForObject(uri, String.class);
        JSONObject obj = new JSONObject(jsonString);
        Set<String> keys = obj.keySet();
        for( String key : keys){
            JSONObject jsonHero = obj.getJSONObject(String.valueOf(key));
            Hero hero = new Hero(jsonHero);
            JSONArray rolesJson = obj.getJSONObject(String.valueOf(key)).getJSONArray("roles");
            List<String > rolesList = rolesJson.toList().stream().map(Object::toString).toList();
            List<Role> roles = roleRepository.findByDescriptionIn(rolesList);
            hero.setRoles(roles);
            heroRepository.save(hero);
        }
    }
}
