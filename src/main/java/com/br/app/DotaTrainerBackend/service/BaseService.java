package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseService {
    @Value("${external-api.url}")
    protected String apiUrl;

    private final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    protected ResponseEntity<String> getFromApi(String uri){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class);
        int statusCode = response.getStatusCode().value();
        if(statusCode!=200){
            LOGGER.error("Error while trying to get data from opendota api,uri ${}, status code: ${}, body: ${}",uri,
                    statusCode,response.getBody());
            throw new ExternalApiException("URI: "+uri+" returned code: "+statusCode);
        }
        return response;
    }
}
