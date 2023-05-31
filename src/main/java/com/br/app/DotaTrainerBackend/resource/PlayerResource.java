package com.br.app.DotaTrainerBackend.resource;

import com.br.app.DotaTrainerBackend.domain.HeroStats;
import com.br.app.DotaTrainerBackend.domain.Player;
import com.br.app.DotaTrainerBackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<List<HeroStats>> getPlayerHeroStatus(@PathVariable Long playerId){
        List<HeroStats> list = playerService.getPlayerHeroStatus(playerId);
        if(!list.isEmpty())
            return ResponseEntity.ok(list);
        else
            return ResponseEntity.notFound().build();

    }
}
