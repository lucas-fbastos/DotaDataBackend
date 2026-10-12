package io.luck.AghsLabs.service;

import io.luck.AghsLabs.model.dtos.ComparisonDtos.CompatibilityReason;
import io.luck.AghsLabs.model.dtos.ComparisonDtos.CompatibilityTier;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchCandidate;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class MatchPairingService {
    public static final int MIN_MATCH_DURATION_SECONDS = 600;

    private static final long SAME_HERO_WEIGHT = 1_000_000_000L;
    private static final long SAME_PATCH_WEIGHT = 100_000_000L;
    private static final long SAME_LANE_WEIGHT = 10_000_000L;
    private static final long SAME_MODE_WEIGHT = 3_000_000L;
    private static final long SAME_LOBBY_WEIGHT = 2_000_000L;
    private static final long DURATION_WEIGHT = 1_000_000L;
    private static final long DATE_WEIGHT = 500_000L;

    private static final Comparator<OpenDotaPlayerMatchCandidate> CANDIDATE_ORDER = Comparator
            .comparing(OpenDotaPlayerMatchCandidate::startTime,
                    Comparator.nullsLast(Comparator.reverseOrder()))
            .thenComparing(OpenDotaPlayerMatchCandidate::matchId,
                    Comparator.nullsLast(Comparator.reverseOrder()));

    public List<PairedCandidate> pair(List<OpenDotaPlayerMatchCandidate> leftCandidates,
                                      List<OpenDotaPlayerMatchCandidate> rightCandidates,
                                      int pairLimit) {
        if (pairLimit <= 0) return List.of();
        List<OpenDotaPlayerMatchCandidate> left = eligible(leftCandidates);
        List<OpenDotaPlayerMatchCandidate> right = eligible(rightCandidates);
        int requestedFlow = Math.min(pairLimit, Math.min(left.size(), right.size()));
        if (requestedFlow == 0) return List.of();

        int source = 0;
        int leftOffset = 1;
        int rightOffset = leftOffset + left.size();
        int sink = rightOffset + right.size();
        List<List<Edge>> graph = graph(sink + 1);

        for (int i = 0; i < left.size(); i++) addEdge(graph, source, leftOffset + i, 1, 0, false);
        for (int j = 0; j < right.size(); j++) addEdge(graph, rightOffset + j, sink, 1, 0, false);
        for (int i = 0; i < left.size(); i++) {
            for (int j = 0; j < right.size(); j++) {
                addEdge(graph, leftOffset + i, rightOffset + j, 1,
                        -quality(left.get(i), right.get(j)).score(), true);
            }
        }

        for (int flow = 0; flow < requestedFlow; flow++) {
            if (!augmentShortestPath(graph, source, sink)) break;
        }

        List<PairedCandidate> result = new ArrayList<>();
        for (int i = 0; i < left.size(); i++) {
            for (Edge edge : graph.get(leftOffset + i)) {
                if (!edge.candidateEdge || edge.capacity != 0) continue;
                int rightIndex = edge.to - rightOffset;
                if (rightIndex < 0 || rightIndex >= right.size()) continue;
                var quality = quality(left.get(i), right.get(rightIndex));
                result.add(PairedCandidate.builder()
                        .left(left.get(i))
                        .right(right.get(rightIndex))
                        .qualityScore(quality.score())
                        .compatibilityTier(quality.tier())
                        .compatibilityReasons(quality.reasons())
                        .deepComparisonAvailable(quality.reasons.contains(CompatibilityReason.SAME_HERO))
                        .build());
            }
        }

        return result.stream()
                .sorted(Comparator.comparingLong(PairedCandidate::qualityScore).reversed()
                        .thenComparing(pair -> pair.left().startTime(), Comparator.reverseOrder())
                        .thenComparing(pair -> pair.left().matchId())
                        .thenComparing(pair -> pair.right().matchId()))
                .limit(requestedFlow)
                .toList();
    }

    public boolean isEligible(OpenDotaPlayerMatchCandidate candidate) {
        if (candidate == null || candidate.matchId() == null || candidate.heroId() == null
                || candidate.playerSlot() == null || candidate.startTime() == null
                || candidate.duration() == null || candidate.duration() < MIN_MATCH_DURATION_SECONDS) {
            return false;
        }
        if (candidate.playerSlot() < 0 || candidate.playerSlot() > 255) return false;
        return candidate.leaverStatus() == null || candidate.leaverStatus() <= 1;
    }

    Quality quality(OpenDotaPlayerMatchCandidate left, OpenDotaPlayerMatchCandidate right) {
        boolean sameHero = Objects.equals(left.heroId(), right.heroId());
        boolean samePatch = nonNullEquals(left.patch(), right.patch());
        boolean sameLane = nonNullEquals(left.laneRole(), right.laneRole());
        boolean sameMode = nonNullEquals(left.gameMode(), right.gameMode());
        boolean sameLobby = nonNullEquals(left.lobbyType(), right.lobbyType());
        long durationDifference = Math.abs((long) left.duration() - right.duration());
        long dateDifference = Math.abs(left.startTime() - right.startTime());

        long score = 0;
        List<CompatibilityReason> reasons = new ArrayList<>();
        if (sameHero) {
            score += SAME_HERO_WEIGHT;
            reasons.add(CompatibilityReason.SAME_HERO);
        }
        if (samePatch) {
            score += SAME_PATCH_WEIGHT;
            reasons.add(CompatibilityReason.SAME_PATCH);
        }
        if (sameLane) {
            score += SAME_LANE_WEIGHT;
            reasons.add(CompatibilityReason.SAME_LANE_ROLE);
        }
        if (sameMode) {
            score += SAME_MODE_WEIGHT;
            reasons.add(CompatibilityReason.SAME_GAME_MODE);
        }
        if (sameLobby) {
            score += SAME_LOBBY_WEIGHT;
            reasons.add(CompatibilityReason.SAME_LOBBY_TYPE);
        }
        long durationScore = Math.max(0, DURATION_WEIGHT - durationDifference * 1_000L);
        score += durationScore;
        if (durationDifference <= 180) reasons.add(CompatibilityReason.SIMILAR_DURATION);

        long differenceHours = dateDifference / 3_600L;
        score += Math.max(0, DATE_WEIGHT - differenceHours * 1_000L);
        if (dateDifference <= 14L * 24 * 3_600) reasons.add(CompatibilityReason.CLOSE_IN_TIME);

        int contextualMatches = (sameLane ? 1 : 0) + (sameMode ? 1 : 0) + (sameLobby ? 1 : 0);
        CompatibilityTier tier;
        if (sameHero && samePatch && sameLane && sameMode && sameLobby) tier = CompatibilityTier.OPTIMAL;
        else if (sameHero) tier = CompatibilityTier.STRONG;
        else if (samePatch && contextualMatches >= 2) tier = CompatibilityTier.COMPATIBLE;
        else tier = CompatibilityTier.WEAK;
        return Quality.builder()
                .score(score)
                .tier(tier)
                .reasons(List.copyOf(reasons))
                .build();
    }

    private List<OpenDotaPlayerMatchCandidate> eligible(List<OpenDotaPlayerMatchCandidate> candidates) {
        if (candidates == null) return List.of();
        return candidates.stream()
                .filter(this::isEligible)
                .sorted(CANDIDATE_ORDER)
                .toList();
    }

    private static boolean nonNullEquals(Object left, Object right) {
        return left != null && left.equals(right);
    }

    private static List<List<Edge>> graph(int size) {
        List<List<Edge>> graph = new ArrayList<>(size);
        for (int i = 0; i < size; i++) graph.add(new ArrayList<>());
        return graph;
    }

    private static void addEdge(List<List<Edge>> graph, int from, int to, int capacity,
                                long cost, boolean candidateEdge) {
        Edge forward = new Edge(to, graph.get(to).size(), capacity, cost, candidateEdge);
        Edge reverse = new Edge(from, graph.get(from).size(), 0, -cost, false);
        graph.get(from).add(forward);
        graph.get(to).add(reverse);
    }

    private static boolean augmentShortestPath(List<List<Edge>> graph, int source, int sink) {
        int size = graph.size();
        long infinity = Long.MAX_VALUE / 4;
        long[] distance = new long[size];
        int[] previousNode = new int[size];
        int[] previousEdge = new int[size];
        Arrays.fill(distance, infinity);
        Arrays.fill(previousNode, -1);
        distance[source] = 0;

        for (int pass = 0; pass < size - 1; pass++) {
            boolean changed = false;
            for (int node = 0; node < size; node++) {
                if (distance[node] == infinity) continue;
                List<Edge> edges = graph.get(node);
                for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
                    Edge edge = edges.get(edgeIndex);
                    if (edge.capacity <= 0) continue;
                    long candidateDistance = distance[node] + edge.cost;
                    if (candidateDistance >= distance[edge.to]) continue;
                    distance[edge.to] = candidateDistance;
                    previousNode[edge.to] = node;
                    previousEdge[edge.to] = edgeIndex;
                    changed = true;
                }
            }
            if (!changed) break;
        }

        if (previousNode[sink] < 0) return false;
        for (int node = sink; node != source; node = previousNode[node]) {
            Edge edge = graph.get(previousNode[node]).get(previousEdge[node]);
            edge.capacity--;
            graph.get(node).get(edge.reverseIndex).capacity++;
        }
        return true;
    }

    @Builder
    public record PairedCandidate(
            OpenDotaPlayerMatchCandidate left,
            OpenDotaPlayerMatchCandidate right,
            long qualityScore,
            CompatibilityTier compatibilityTier,
            List<CompatibilityReason> compatibilityReasons,
            boolean deepComparisonAvailable
    ) {}

    @Builder
    record Quality(
            long score,
            CompatibilityTier tier,
            List<CompatibilityReason> reasons
    ) {}

    private static final class Edge {
        private final int to;
        private final int reverseIndex;
        private int capacity;
        private final long cost;
        private final boolean candidateEdge;

        private Edge(int to, int reverseIndex, int capacity, long cost, boolean candidateEdge) {
            this.to = to;
            this.reverseIndex = reverseIndex;
            this.capacity = capacity;
            this.cost = cost;
            this.candidateEdge = candidateEdge;
        }
    }
}
