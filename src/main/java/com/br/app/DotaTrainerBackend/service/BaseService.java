package com.br.app.DotaTrainerBackend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class BaseService {
    @Value("${external-api.url}")
    protected String apiUrl;

    protected String getFromApi(String uri){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }
}
