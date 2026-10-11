package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import tools.jackson.databind.JsonNode;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData.*;

@Builder
public record OpenDotaPlayerMatchDetails(
        Integer version,
        @JsonProperty("match_id") Long matchId,
        List<Teamfight> teamfights,
        List<Pause> pauses,
        List<Objective> objectives,
        List<Chat> chat,
        @JsonProperty("radiant_gold_adv") List<Long> radiantGoldAdv,
        @JsonProperty("radiant_xp_adv") List<Long> radiantXpAdv,
        Map<String, JsonNode> cosmetics,
        @JsonProperty("draft_timings") List<DraftTiming> draftTimings,
        List<OpenDotaMatchPlayer> players,
        @JsonProperty("leagueid") Long leagueId,
        @JsonProperty("start_time") Long startTime,
        Long duration,
        @JsonProperty("series_id") Long seriesId,
        @JsonProperty("series_type") Integer seriesType,
        Integer cluster,
        @JsonProperty("replay_salt") Long replaySalt,
        @JsonProperty("radiant_win") Boolean radiantWin,
        @JsonProperty("pre_game_duration") Integer preGameDuration,
        @JsonProperty("match_seq_num") Long matchSeqNum,
        @JsonProperty("tower_status_radiant") Integer towerStatusRadiant,
        @JsonProperty("tower_status_dire") Integer towerStatusDire,
        @JsonProperty("barracks_status_radiant") Integer barracksStatusRadiant,
        @JsonProperty("barracks_status_dire") Integer barracksStatusDire,
        @JsonProperty("first_blood_time") Integer firstBloodTime,
        @JsonProperty("lobby_type") Integer lobbyType,
        @JsonProperty("human_players") Integer humanPlayers,
        @JsonProperty("game_mode") Integer gameMode,
        Integer flags,
        Integer engine,
        @JsonProperty("radiant_score") Integer radiantScore,
        @JsonProperty("dire_score") Integer direScore,
        @JsonProperty("radiant_team_id") Long radiantTeamId,
        @JsonProperty("radiant_name") String radiantName,
        @JsonProperty("radiant_logo") BigInteger radiantLogo,
        @JsonProperty("radiant_team_complete") Integer radiantTeamComplete,
        @JsonProperty("dire_team_id") Long direTeamId,
        @JsonProperty("dire_name") String direName,
        @JsonProperty("dire_logo") BigInteger direLogo,
        @JsonProperty("dire_team_complete") Integer direTeamComplete,
        @JsonProperty("radiant_captain") Long radiantCaptain,
        @JsonProperty("dire_captain") Long direCaptain,
        @JsonProperty("picks_bans") List<PickBan> picksBans,
        @JsonProperty("od_data") OdData odData,
        League league,
        @JsonProperty("radiant_team") TeamInfo radiantTeam,
        @JsonProperty("dire_team") TeamInfo direTeam,
        JsonNode metadata,
        @JsonProperty("replay_url") String replayUrl,
        Integer patch,
        Integer region,
        @JsonProperty("all_word_counts") Map<String, Long> allWordCounts,
        @JsonProperty("my_word_counts") Map<String, Long> myWordCounts,
        @JsonProperty("throw") Integer throwAmount,
        Integer loss
) {}
