package io.luck.AghsLabs.config;

import io.luck.AghsLabs.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;

@Configuration
public class WebClientConfig {

    private final String apiUrl;

    public WebClientConfig(@Value("${external-api.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiUrl)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    Logger.getLogger(WebClientConfig.class.getName())
                            .warning("Client error - URL: " + request.getURI() +
                                    " - Status: " + response.getStatusCode());
                    throw new ExternalApiException(
                            "OpenDota rejected request with status " + response.getStatusCode());
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    Logger.getLogger(WebClientConfig.class.getName())
                            .severe("Server error - URL: " + request.getURI() +
                                    " - Status: " + response.getStatusCode());
                    throw new ExternalApiException(
                            "OpenDota failed with status " + response.getStatusCode()
                    );
                })
                .build();
    }

}
