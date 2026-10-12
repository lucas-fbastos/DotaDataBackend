package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.exception.BadRequestException;
import io.luck.AghsLabs.model.Enums.SearchKind;
import io.luck.AghsLabs.model.dtos.PlayerSearchResult;
import io.luck.AghsLabs.service.PlayerSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping({"/players", "/player"})
public class PlayerSearchResource {

    private static final int MIN_QUERY_LENGTH = 2;
    private static final int MAX_QUERY_LENGTH = 64;
    private final PlayerSearchService playerSearchService;

    public PlayerSearchResource(PlayerSearchService playerSearchService) {
        this.playerSearchService = playerSearchService;
    }

    @GetMapping("/search")
    public List<PlayerSearchResult> searchPlayers(
            @RequestParam String q,
            @RequestParam(defaultValue = "any") String kind
    ) {
        String query = validateQuery(q);
        SearchKind searchKind = SearchKind.parse(kind);
        return playerSearchService.search(query, searchKind);
    }


    private static String validateQuery(String rawQuery) {
        if (rawQuery == null) throw new BadRequestException("Query parameter 'q' is required");
        String query = rawQuery.trim();
        if (query.length() < MIN_QUERY_LENGTH || query.length() > MAX_QUERY_LENGTH) {
            throw new BadRequestException("Query parameter 'q' must contain between 2 and 64 characters");
        }
        return query;
    }
}
