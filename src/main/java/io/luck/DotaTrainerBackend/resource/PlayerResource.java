package io.luck.DotaTrainerBackend.resource;

import io.luck.DotaTrainerBackend.model.HeroStats;
import io.luck.DotaTrainerBackend.model.entity.Player;
import io.luck.DotaTrainerBackend.providers.opendota.model.OpenDotaHeroStats;
import io.luck.DotaTrainerBackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PlayerService playerService;

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
