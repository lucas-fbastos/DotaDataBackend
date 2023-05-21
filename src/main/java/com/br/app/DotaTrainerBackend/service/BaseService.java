package com.br.app.DotaTrainerBackend.service;

import com.br.app.DotaTrainerBackend.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseService {
    @Value("${external-api.url}")
    protected String apiUrl;

    protected ResponseEntity<String> getFromApi(String uri){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class);
        int statusCode = response.getStatusCode().value();
        if(statusCode!=200){
            throw new ExternalApiException("URI: "+uri+" returned code: "+statusCode);
        }
        return response;
    }
}
