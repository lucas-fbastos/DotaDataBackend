package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.Match;
import io.luck.AghsLabs.model.MatchSummary;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerRatio;
import io.luck.AghsLabs.service.MatchService;
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

    private final MatchService matchService;

    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{playerId}/recent")
    public List<MatchSummary> getRecentMatchesByPlayer(@PathVariable long playerId){
        return this.matchService.getRecentMatches(playerId);
    }

    @GetMapping("/{playerId}/winLose")
    public OpenDotaPlayerRatio getTotalWinLoseByPlayer(@PathVariable long playerId){
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
