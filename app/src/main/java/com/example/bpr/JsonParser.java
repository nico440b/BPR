package com.example.bpr;

import java.lang.reflect.Type;

public interface JsonParser {
    public String fromJson(String json, Type type);
    public String toJson(Object object,Type type);
}
