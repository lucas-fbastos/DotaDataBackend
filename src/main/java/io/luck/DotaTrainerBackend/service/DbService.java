package io.luck.DotaTrainerBackend.service;

import io.luck.DotaTrainerBackend.model.entity.*;
import io.luck.DotaTrainerBackend.repository.*;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaProPlayer;
import io.luck.DotaTrainerBackend.providers.opendota.service.OpenDotaProPlayerDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


@Service
public class DbService extends BaseService{

    private final HeroRepository heroRepository;
    private final RoleRepository roleRepository;
    private final ProPlayerRepository proPlayerRepository;
    private final OpenDotaProPlayerDataService openDotaProPlayerDataService;
    private final ItemAttributeRepository itemAttributeRepository;
    private final ItemRepository itemRepository;
    private final PatchRepository patchRepository;

    private final Logger LOGGER = Logger.getLogger(DbService.class.getName());

    private static final String[] ROLES = {"Carry","Nuker","Initiator","Disabler","Support","Escape","Durable","Pusher"};

    public DbService(
            HeroRepository heroRepository,
            RoleRepository roleRepository,
            ProPlayerRepository proPlayerRepository,
            OpenDotaProPlayerDataService openDotaProPlayerDataService,
            ItemAttributeRepository itemAttributeRepository,
            ItemRepository itemRepository,
            PatchRepository patchRepository,
            @Value("${external-api.url}") String apiUrl
    ) {
        super(apiUrl);
        this.heroRepository = heroRepository;
        this.roleRepository = roleRepository;
        this.proPlayerRepository = proPlayerRepository;
        this.openDotaProPlayerDataService = openDotaProPlayerDataService;
        this.itemAttributeRepository = itemAttributeRepository;
        this.itemRepository = itemRepository;
        this.patchRepository = patchRepository;
    }

    public void instantiateTestDatabase(){
        seedHeroRoles();
        saveHeroesFromApi();
        seedProPlayers();
        seedItems();
        seedPatch();
    }

    private void seedHeroRoles(){ for(String role : ROLES) roleRepository.save(new Role(role));}

    public void seedProPlayers(){
        List<ProPlayer> proPlayersToPersist = openDotaProPlayerDataService.getProPlayers().stream()
                .map(this::toProPlayerEntity)
                .toList();
        this.proPlayerRepository.saveAll(proPlayersToPersist);
    }

    private ProPlayer toProPlayerEntity(OpenDotaProPlayer player) {
        ProPlayer proPlayer = new ProPlayer(
                player.teamName(),
                player.teamTag(),
                player.fantasyRole(),
                player.teamId(),
                player.isLocked(),
                player.lockedUntil()
        );

        proPlayer.setAccountId(player.accountId());
        proPlayer.setMmrEstimate(player.computedMmr());
        proPlayer.setPersonaName(player.personaName());
        proPlayer.setName(player.name());
        proPlayer.setCheese(player.cheese());
        proPlayer.setSteamId(player.steamId());
        proPlayer.setAvatarImage(player.avatar());
        proPlayer.setAvatarImageMedium(player.avatarMedium());
        proPlayer.setAvatarImageFull(player.avatarFull());
        proPlayer.setProfileURL(player.profileUrl());
        proPlayer.setLastLogin(player.lastLogin());
        proPlayer.setLocCountryCode(player.locCountryCode());

        return proPlayer;
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
