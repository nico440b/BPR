package com.example.bpr;




import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;


import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.Department;
import com.example.bpr.Objects.Link;
import com.example.bpr.Objects.Location;
import com.example.bpr.Objects.OpeningHour;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ProvidedTypeConverter
public class Converters {
    public Gson gson = new Gson();



    @TypeConverter
    public static List<Link> fromList(String list) {
        if (list == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Link>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(list, listType);
    }
    @TypeConverter
    public static String someObjectListToString(List<Link> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
    @TypeConverter
    public static List<Department> fromDepartmentList(String list) {
        if (list == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Department>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(list, listType);
    }
    @TypeConverter
    public static String someDepartmentListToString(List<Department> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
    @TypeConverter
    public static List<OpeningHour> fromOpeninghourList(String list) {
        if (list == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<OpeningHour>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(list, listType);
    }
    @TypeConverter
    public static String someOpeninghourListToString(List<OpeningHour> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
    @TypeConverter
    public static Location fromLocation(String list) {
        if (list == null) {
            return null;
        }

        Type listType = new TypeToken<List<Location>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(list, listType);
    }
    @TypeConverter
    public static String someLocationListToString(Location someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public static String fromArrayList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
    @TypeConverter
    public static List<CoopStore> fromCoopstoreList(String list) {
        if (list == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Link>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(list, listType);
    }
    @TypeConverter
    public static String someCoopstoreListToString(List<CoopStore> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }



}
