package com.br.app.DotaTrainerBackend.resource;

import com.br.app.DotaTrainerBackend.domain.Match;
import com.br.app.DotaTrainerBackend.domain.MatchSummary;
import com.br.app.DotaTrainerBackend.service.DbService;
import com.br.app.DotaTrainerBackend.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matches")
public class MatchResource {


    @Autowired
    private MatchService matchService;

    @Autowired
    private DbService dbService;

    @GetMapping("/{playerId}/recent")
    public List<MatchSummary> getRecentMatchesByPlayer(@PathVariable long playerId){
        return this.matchService.getRecentMatches(playerId);
    }

    @GetMapping("/{playerId}/winLose")
    public Map<String,Integer> getTotalWinLoseByPlayer(@PathVariable long playerId){
        return this.matchService.getWinLose(playerId);
    }

    @GetMapping("details/{matchId}")
    public ResponseEntity<Match> getMatchDetails(@PathVariable long matchId){
        Match match = this.matchService.getMatchDetails(matchId);
       if(match!=null)
           return ResponseEntity.ok(match);
       else
           return ResponseEntity.notFound().build();
    }
}
