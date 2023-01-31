package com.br.app.DotaTrainerBackend.resource;

import com.br.app.DotaTrainerBackend.model.MatchSummary;
import com.br.app.DotaTrainerBackend.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchResource {


    @Autowired
    private MatchService matchService;

    @GetMapping("/{playerId}/recent")
    public List<MatchSummary> getRecentMatchesByPlayer(@PathVariable long playerId){
        return this.matchService.getRecentMatches(playerId);
    }

}
