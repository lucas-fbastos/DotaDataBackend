package com.br.app.DotaTrainerBackend.mapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface Mapper<T> {

    public T convert(JSONObject json);

    public List<T> convert(JSONArray jsonArray);
}
