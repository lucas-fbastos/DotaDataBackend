package io.luck.AghsLabs.providers.opendota.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

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
                        {"5003":"antimage_mana_break"}
                        """, MediaType.APPLICATION_JSON));
        var service = new OpenDotaAbilityDataService(builder.build());

        var result = service.getIds();

        assertEquals(1, result.size());
        assertEquals(5003L, result.getFirst().id());
        assertEquals("antimage_mana_break", result.getFirst().name());
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
