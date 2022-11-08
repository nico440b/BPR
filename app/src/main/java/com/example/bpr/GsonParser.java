package com.example.bpr;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonParser implements JsonParser{
    private Gson gson;
    @Override
    public String fromJson(String json, Type type) {
        return gson.fromJson(json,type);
    }

    @Override
    public String toJson(Object object, Type type) {
        return gson.toJson(object,type);
    }
}
