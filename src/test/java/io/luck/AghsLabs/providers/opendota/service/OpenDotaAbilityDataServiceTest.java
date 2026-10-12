package io.luck.AghsLabs.providers.opendota.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class OpenDotaAbilityDataServiceTest {

    @Test
    void getsAbilityIds() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.opendota.test/api/");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.opendota.test/api/constants/ability_ids"))
                .andRespond(withSuccess("""
                        {
                          "5003":"antimage_mana_break",
                          "3060,1617":"templar_assassin_hidden_gates"
                        }
                        """, MediaType.APPLICATION_JSON));
        var service = new OpenDotaAbilityDataService(builder.build());

        var result = service.getIds();

        Map<Long, String> idsByName = result.stream().collect(Collectors.toMap(
                ability -> ability.id(),
                ability -> ability.name()
        ));
        assertEquals(Map.of(
                5003L, "antimage_mana_break",
                3060L, "templar_assassin_hidden_gates",
                1617L, "templar_assassin_hidden_gates"
        ), idsByName);
        server.verify();
    }

    @Test
    void getsAbilityData() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.opendota.test/api/");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.opendota.test/api/constants/abilities"))
                .andRespond(withSuccess("""
                        {
                          "antimage_mana_break": {
                            "dname": "Mana Break",
                            "img": "/apps/dota2/images/dota_react/abilities/antimage_mana_break.png"
                          }
                        }
                        """, MediaType.APPLICATION_JSON));
        var service = new OpenDotaAbilityDataService(builder.build());

        var result = service.getAll();

        assertEquals("Mana Break", result.get("antimage_mana_break").displayName());
        assertEquals(
                "/apps/dota2/images/dota_react/abilities/antimage_mana_break.png",
                result.get("antimage_mana_break").img()
        );
        server.verify();
    }
}
