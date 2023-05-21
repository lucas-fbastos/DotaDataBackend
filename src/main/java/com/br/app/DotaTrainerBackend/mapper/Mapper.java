package com.br.app.DotaTrainerBackend.mapper;

import org.json.JSONObject;

public interface Mapper<T> {

    public T convert(JSONObject json);
}
