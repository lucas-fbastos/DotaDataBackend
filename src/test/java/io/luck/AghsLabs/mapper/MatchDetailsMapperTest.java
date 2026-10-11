package io.luck.AghsLabs.mapper;

import io.luck.AghsLabs.model.Enums.GameMode;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchDetailsData;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaMatchPlayer;
import io.luck.AghsLabs.providers.opendota.model.OpenDotaPlayerMatchDetails;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatchDetailsMapperTest {

    @Test
    void chatIdentifiesAccountsByPlayerSlotAndKeepsUnidentifiedEntries() {
        var mapper = new MatchDetailsMapper(new MatchPlayerMapper());
        var source = OpenDotaPlayerMatchDetails.builder()
                .players(List.of(
                        OpenDotaMatchPlayer.builder().playerSlot(128).accountId(84L).build(),
                        OpenDotaMatchPlayer.builder().playerSlot(0).accountId(42L).build(),
                        OpenDotaMatchPlayer.builder().playerSlot(1).build(),
                        OpenDotaMatchPlayer.builder().accountId(99L).build()))
                .chat(List.of(
                        new OpenDotaMatchDetailsData.Chat(60, "chat", "hi", 0, 0),
                        new OpenDotaMatchDetailsData.Chat(61, "chatwheel", "49", 5, 128),
                        new OpenDotaMatchDetailsData.Chat(62, "chat", "gg", 1, 1),
                        new OpenDotaMatchDetailsData.Chat(63, "chat", "unknown", 9, 132),
                        new OpenDotaMatchDetailsData.Chat(64, "chat", "observer", 10, null)))
                .build();

        var chat = mapper.convertChat(source).chat();

        assertEquals(Arrays.asList(42L, 84L, null, null, null),
                chat.stream().map(entry -> entry.accountId()).toList());
        assertEquals(List.of("hi", "49", "gg", "unknown", "observer"),
                chat.stream().map(entry -> entry.key()).toList());
    }

    @Test
    void chatWithoutPlayerDataKeepsEntriesWithUnknownAccounts() {
        var mapper = new MatchDetailsMapper(new MatchPlayerMapper());
        var source = OpenDotaPlayerMatchDetails.builder()
                .chat(List.of(new OpenDotaMatchDetailsData.Chat(60, "chat", "gg", 0, 0)))
                .build();

        var chat = mapper.convertChat(source).chat();

        assertEquals(1, chat.size());
        assertNull(chat.getFirst().accountId());
        assertNull(mapper.convertChat(OpenDotaPlayerMatchDetails.builder().build()).chat());
    }

    private static final Set<String> CHAT_FIELDS = Set.of("chat", "all_word_counts", "my_word_counts");
    private static final Set<String> WARD_FIELDS = Set.of("obs_log", "sen_log", "obs_left_log", "sen_left_log");

    // These keys identify units, abilities, items, coordinates, or counter categories.
    private static final Set<String> DICTIONARIES = Set.of(
            "cosmetics", "metadata", "lane_pos", "obs", "sen", "actions", "purchase",
            "gold_reasons", "xp_reasons", "killed", "item_uses", "ability_uses",
            "ability_targets", "damage_targets", "hero_hits", "damage", "damage_taken",
            "damage_inflictor", "runes", "killed_by", "kill_streaks", "multi_kills",
            "life_state", "healing", "damage_inflictor_received", "purchase_time",
            "first_purchase_time", "item_win", "item_usage", "deaths_pos",
            "all_word_counts", "my_word_counts"
    );

    @Test
    void sampleFieldsSurviveParsingAndMappingWithCamelCaseResponseNames() throws IOException {
        var jsonMapper = JsonMapper.builder().build();
        JsonNode source;
        try (var input = getClass().getResourceAsStream("/opendota/match-details.json")) {
            assertNotNull(input);
            source = jsonMapper.readTree(input);
        }
        var providerMatch = jsonMapper.readValue(source.toString(), OpenDotaPlayerMatchDetails.class);
        var providerJson = jsonMapper.readTree(jsonMapper.writeValueAsString(providerMatch));
        assertValues(source, providerJson, "$", false);

        var mapper = new MatchDetailsMapper(new MatchPlayerMapper());
        var match = mapper.convert(providerMatch, Map.of(), Map.of());
        var response = jsonMapper.readTree(jsonMapper.writeValueAsString(match));
        assertValues(source, response, "$", true);
        assertEquals(source.get("duration").longValue(), match.getDuration().toSeconds());
        assertEquals(GameMode.getById(source.get("game_mode").intValue()).orElseThrow(), match.getGameMode());
        assertEquals(5, match.getRadiantTeam().size());
        assertEquals(5, match.getDireTeam().size());
        assertEquals(6, match.getPlayers().getFirst().items().size());
        assertEquals(match.getPlayers().getFirst().heroHealing(), match.getPlayers().getFirst().heroHeal());
        assertFalse(response.has("providerFields"));
        assertFalse(response.get("players").get(0).has("providerFields"));
        assertFalse(response.has("chat"));
        assertFalse(response.has("allWordCounts"));
        assertFalse(response.has("myWordCounts"));

        var chat = jsonMapper.readTree(jsonMapper.writeValueAsString(mapper.convertChat(providerMatch)));
        assertEquals(3, chat.size());
        assertValues(source.get("chat"), chat.get("chat"), "$.chat", true);
        assertValues(source.get("all_word_counts"), chat.get("allWordCounts"), "$.allWordCounts", false);
        assertValues(source.get("my_word_counts"), chat.get("myWordCounts"), "$.myWordCounts", false);

        var wards = jsonMapper.readTree(jsonMapper.writeValueAsString(mapper.convertWards(providerMatch)));
        assertEquals(1, wards.size());
        assertEquals(source.get("players").size(), wards.get("players").size());
        for (int i = 0; i < source.get("players").size(); i++) {
            var providerPlayer = source.get("players").get(i);
            var wardPlayer = wards.get("players").get(i);
            assertEquals(7, wardPlayer.size());
            for (var field : Set.of("player_slot", "account_id", "hero_id")) {
                assertValues(providerPlayer.get(field), wardPlayer.get(responseName(field, false)),
                        "$.wards.players[" + i + "]." + field, true);
            }
            for (var field : WARD_FIELDS) {
                assertValues(providerPlayer.get(field), wardPlayer.get(responseName(field, false)),
                        "$.wards.players[" + i + "]." + field, true);
            }
        }
        for (var group : Set.of("players", "radiantTeam", "direTeam")) {
            for (var player : response.get(group)) {
                for (var field : WARD_FIELDS) {
                    assertFalse(player.has(responseName(field, false)), group + "." + field);
                }
            }
        }
    }

    private void assertValues(JsonNode expected, JsonNode actual, String path, boolean camelCase) {
        assertNotNull(actual, "Missing field: " + path);
        if (expected.isObject()) {
            assertTrue(actual.isObject(), path);
            for (var field : expected.properties()) {
                // The existing app contract represents these two values as Duration and GameMode.
                if (camelCase && path.equals("$") && Set.of("duration", "game_mode").contains(field.getKey())) continue;
                if (camelCase && path.equals("$") && CHAT_FIELDS.contains(field.getKey())) continue;
                if (camelCase && WARD_FIELDS.contains(field.getKey())) continue;
                var name = camelCase ? responseName(field.getKey(), path.equals("$")) : field.getKey();
                assertValues(field.getValue(), actual.get(name), path + "." + name,
                        camelCase && !DICTIONARIES.contains(field.getKey()));
            }
        } else if (expected.isArray()) {
            assertTrue(actual.isArray(), path);
            assertEquals(expected.size(), actual.size(), path);
            for (int i = 0; i < expected.size(); i++) {
                assertValues(expected.get(i), actual.get(i), path + "[" + i + "]", camelCase);
            }
        } else if (expected.isNumber()) {
            assertTrue(actual.isNumber(), path);
            assertEquals(0, expected.decimalValue().compareTo(actual.decimalValue()), path);
        } else {
            assertEquals(expected, actual, path);
        }
    }

    private String responseName(String name, boolean root) {
        if (root && name.equals("radiant_team")) return "radiantTeamInfo";
        if (root && name.equals("dire_team")) return "direTeamInfo";
        return switch (name) {
            case "leagueid" -> "leagueId";
            case "personaname" -> "personaName";
            case "firstblood_claimed" -> "firstBloodClaimed";
            case "networth_t" -> "netWorthT";
            case "throw" -> "throwAmount";
            case "ehandle" -> "entityHandle";
            case "entityleft" -> "entityLeft";
            case "attackername" -> "attackerName";
            case "has_gcdata" -> "hasGcData";
            default -> {
                var camel = new StringBuilder();
                boolean uppercase = false;
                for (char c : name.toCharArray()) {
                    if (c == '_') uppercase = true;
                    else {
                        camel.append(uppercase ? Character.toUpperCase(c) : c);
                        uppercase = false;
                    }
                }
                yield camel.toString();
            }
        };
    }
}
