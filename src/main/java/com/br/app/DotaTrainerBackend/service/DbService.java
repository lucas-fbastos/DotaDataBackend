package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.model.*;
import com.br.app.DotaTrainerBackend.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


@Service
public class DbService extends BaseService{

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProPlayerRepository proPlayerRepository;

    @Autowired
    private ItemAttributeRepository itemAttributeRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final Logger LOGGER = Logger.getLogger(DbService.class.getName());

    private final String[] ROLES = {"Carry","Nuker","Initiator","Disabler","Support","Escape","Durable","Pusher"};

    public void instantiateTestDatabase(){
        seedHeroRoles();
        saveHeroesFromApi();
        seedProPlayers();
    }

    private void seedHeroRoles(){ for(String role : ROLES) roleRepository.save(new Role(role));}

    public void seedProPlayers(){
        ResponseEntity<String> proPlayersFromAPI = this.getFromApi(this.apiUrl+ "proPlayers");
        int httpStatus = proPlayersFromAPI.getStatusCode().value();
        if(httpStatus!=200){
            LOGGER.warning("API RETURNED "+httpStatus);
            return;
        }
        JSONArray proObj = new JSONArray(proPlayersFromAPI.getBody());
        List<ProPlayer> proPlayersToPersist = new ArrayList<>();
        proObj.forEach(p -> proPlayersToPersist.add(new ProPlayer((JSONObject) p)));
        this.proPlayerRepository.saveAll(proPlayersToPersist);
    }

    private void saveHeroesFromApi(){
        ResponseEntity<String> heroesFromAPI = this.getFromApi(this.apiUrl + "constants/heroes");
        int httpStatus =  heroesFromAPI.getStatusCode().value();
        if(httpStatus != 200){
            LOGGER.warning("API RETURNED "+httpStatus);
            return;
        }

        JSONObject heroesObj = new JSONObject(heroesFromAPI.getBody());
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

    private void seedItems(){
        ResponseEntity<String> itemsFromApi = this.getFromApi(this.apiUrl + "constants/items");
        int httpStatus =  itemsFromApi.getStatusCode().value();
        if(httpStatus != 200){
            LOGGER.warning("API RETURNED "+httpStatus);
            return;
        }
        JSONObject itemsObj = new JSONObject(itemsFromApi.getBody());
        Set<String> keys = itemsObj.keySet();
        for( String key : keys){
            JSONObject jsonItem = itemsObj.getJSONObject(String.valueOf(key));
            Item item = new Item(jsonItem);
            JSONArray attributes = jsonItem.getJSONArray("attrib");
            List<ItemAttribute> attributeList = new LinkedList<>();
            for (int i = 0; i < attributes.length() ; i++) {
                JSONObject attribute = attributes.getJSONObject(i);
                attributeList.add(new ItemAttribute(attribute));
            }

            if(!attributeList.isEmpty())
              attributeList =  this.itemAttributeRepository.saveAll(attributeList);

            item.setAttributes(attributeList);
            itemRepository.save(item);
        }
    }
}
