package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.HeroStats;
import io.luck.AghsLabs.model.entity.Player;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaHeroStats;
import io.luck.AghsLabs.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerResource {

    private final PlayerService playerService;

    public PlayerResource(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long playerId){
        Player player = playerService.getPlayerById(playerId);
        if (player!=null){
            return ResponseEntity.ok(player);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{playerId}/heroes")
    public ResponseEntity<List<HeroStats>> getPlayerHeroes(@PathVariable Long playerId,
                                                                   @RequestParam(defaultValue = "3") Integer pageSize,
                                                                   @RequestParam(defaultValue = "0") Integer page){
        List<HeroStats> list = playerService.getPlayerHeroes(playerId,pageSize,page);
        if(!list.isEmpty())
            return ResponseEntity.ok(list);
        else
            return ResponseEntity.notFound().build();

    }

}
