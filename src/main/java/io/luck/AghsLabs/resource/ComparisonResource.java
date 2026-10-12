package io.luck.AghsLabs.resource;

import io.luck.AghsLabs.exception.BadRequestException;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.ComparisonOverview;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.DeepComparison;
import io.luck.AghsLabs.service.ComparisonService;
import io.luck.AghsLabs.service.DeepComparisonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compare")
public class ComparisonResource {
    private final ComparisonService comparisonService;
    private final DeepComparisonService deepComparisonService;

    public static final int MAX_CANDIDATE_LIMIT = 30;
    public static final int MAX_PAIR_LIMIT = 10;

    public ComparisonResource(ComparisonService comparisonService,
                              DeepComparisonService deepComparisonService) {
        this.comparisonService = comparisonService;
        this.deepComparisonService = deepComparisonService;
    }

    @GetMapping("/players/{leftId}/{rightId}")
    public ComparisonOverview comparePlayers(
            @PathVariable Long leftId,
            @PathVariable Long rightId,
            @RequestParam(defaultValue = "30") int candidateLimit,
            @RequestParam(defaultValue = "10") int pairLimit
    ) {
        validateOverviewRequest(leftId, rightId, candidateLimit, pairLimit);

        return comparisonService.getOverview(leftId, rightId, candidateLimit, pairLimit);
    }

    @GetMapping("/matches")
    public DeepComparison compareMatches(
            @RequestParam Long leftPlayerId,
            @RequestParam Long leftMatchId,
            @RequestParam Integer leftSlot,
            @RequestParam Long rightPlayerId,
            @RequestParam Long rightMatchId,
            @RequestParam Integer rightSlot
    ) {
        validateRequest(leftPlayerId, leftMatchId, leftSlot, rightPlayerId, rightMatchId, rightSlot);

        return deepComparisonService.compare(
                leftPlayerId,
                leftMatchId,
                leftSlot,
                rightPlayerId,
                rightMatchId,
                rightSlot
        );
    }

    private void validateOverviewRequest(Long leftPlayerId, Long rightPlayerId, int candidateLimit, int pairLimit) {
        validatePlayersId(leftPlayerId,rightPlayerId);

        if (candidateLimit < 1 || candidateLimit > MAX_CANDIDATE_LIMIT) {
            throw new BadRequestException("candidateLimit must be between 1 and 30");
        }
        if (pairLimit < 1 || pairLimit > MAX_PAIR_LIMIT) {
            throw new BadRequestException("pairLimit must be between 1 and 10");
        }
        if (pairLimit > candidateLimit) {
            throw new BadRequestException("pairLimit cannot exceed candidateLimit");
        }
    }

    private void validateRequest(Long leftPlayerId, Long leftMatchId, Integer leftSlot,
                                 Long rightPlayerId, Long rightMatchId, Integer rightSlot) {
        validatePlayersId(leftPlayerId,rightPlayerId);

        if (leftMatchId == null || leftMatchId <= 0 || rightMatchId == null || rightMatchId <= 0) {
            throw new BadRequestException("Both match IDs must be positive");
        }
        validateSlot(leftSlot, "leftSlot");
        validateSlot(rightSlot, "rightSlot");
    }

    private void validatePlayersId(Long leftPlayerId, Long rightPlayerId) {
        if (leftPlayerId == null || leftPlayerId <= 0 || rightPlayerId == null || rightPlayerId <= 0) {
            throw new BadRequestException("Both player IDs must be positive");
        }
        if (leftPlayerId.equals(rightPlayerId)) throw new BadRequestException("Choose two different players");
    }

    private void validateSlot(Integer slot, String name) {
        if (slot == null || slot < 0 || slot > 255) {
            throw new BadRequestException(name + " must be between 0 and 255");
        }
    }
}
