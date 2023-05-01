package com.br.app.DotaTrainerBackend.model;

import java.util.HashMap;

public class Item {
    private String name;
    private Integer cost;
    private String image;
    private Integer id;
    private Integer cd;
    private HashMap<String,String> attributes = new HashMap<>();
    private Integer charges;
}
