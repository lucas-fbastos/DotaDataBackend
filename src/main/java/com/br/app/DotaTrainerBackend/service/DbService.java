package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.domain.*;
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

    @Autowired
    private PatchRepository patchRepository;

    private final Logger LOGGER = Logger.getLogger(DbService.class.getName());

    private final String[] ROLES = {"Carry","Nuker","Initiator","Disabler","Support","Escape","Durable","Pusher"};

    public void instantiateTestDatabase(){
        seedHeroRoles();
        saveHeroesFromApi();
        seedProPlayers();
        seedItems();
        seedPatch();
    }

    private void seedHeroRoles(){ for(String role : ROLES) roleRepository.save(new Role(role));}

    public void seedProPlayers(){
        ResponseEntity<String> proPlayersFromAPI = this.getFromApi(this.apiUrl+ "proPlayers");
        JSONArray proObj = new JSONArray(proPlayersFromAPI.getBody());
        List<ProPlayer> proPlayersToPersist = new ArrayList<>();
        proObj.forEach(p -> proPlayersToPersist.add(new ProPlayer((JSONObject) p)));
        this.proPlayerRepository.saveAll(proPlayersToPersist);
    }

    private void saveHeroesFromApi(){
        ResponseEntity<String> heroesFromAPI = this.getFromApi(this.apiUrl + "constants/heroes");
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
        JSONObject itemsObj = new JSONObject(itemsFromApi.getBody());
        Set<String> keys = itemsObj.keySet();
        for( String key : keys){
            JSONObject jsonItem = itemsObj.getJSONObject(String.valueOf(key));
            Item item = new Item(jsonItem);
            item = itemRepository.save(item);
            JSONArray attributes = jsonItem.getJSONArray("attrib");
            List<ItemAttribute> attributeList = new LinkedList<>();
            for (int i = 0; i < attributes.length() ; i++) {
                JSONObject attribute = attributes.getJSONObject(i);
                attributeList.add(new ItemAttribute(attribute,item));
            }

            if(!attributeList.isEmpty())
                this.itemAttributeRepository.saveAll(attributeList);
        }
    }

    private void seedPatch(){
        ResponseEntity<String> patchesFromApiResponse = this.getFromApi(this.apiUrl + "constants/patch");
        JSONArray patchesObj = new JSONArray(patchesFromApiResponse.getBody());
        List<Patch> patchesToPersist = new ArrayList<>();
        patchesObj.forEach(p -> patchesToPersist.add(new Patch((JSONObject) p)));
        this.patchRepository.saveAll(patchesToPersist);
    }
}
