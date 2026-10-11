package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.model.*;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerRatio;
import io.luck.AghsLabs.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class MatchResource {

    private final MatchService matchService;

    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/matches/{playerId}/recent")
    public List<MatchSummary> getRecentMatchesByPlayer(@PathVariable long playerId){
        return this.matchService.getRecentMatches(playerId);
    }

    @GetMapping("/matches/{playerId}/winLose")
    public OpenDotaPlayerRatio getTotalWinLoseByPlayer(@PathVariable long playerId){
        return this.matchService.getWinLose(playerId);
    }

    @GetMapping("/matches/details/{matchId}")
    public ResponseEntity<Match> getMatchDetails(@PathVariable long matchId){
        Match match = this.matchService.getMatchDetails(matchId);
       if(match!=null)
           return ResponseEntity.ok(match);
       else
           return ResponseEntity.notFound().build();
    }

    @GetMapping("/match/{id}/chat")
    public ResponseEntity<MatchChat> getMatchChat(@PathVariable long id) {
        MatchChat chat = this.matchService.getMatchChat(id);
        return chat != null ? ResponseEntity.ok(chat) : ResponseEntity.notFound().build();
    }

    @GetMapping("/match/{id}/wards")
    public ResponseEntity<MatchWards> getMatchWards(@PathVariable long id) {
        MatchWards wards = this.matchService.getMatchWards(id);
        return wards != null ? ResponseEntity.ok(wards) : ResponseEntity.notFound().build();
    }

    @GetMapping("/matches/live")
    public ResponseEntity<Set<LiveMatch>> getMatchLive(@RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(matchService.getLiveMatches(limit));
    }
}
