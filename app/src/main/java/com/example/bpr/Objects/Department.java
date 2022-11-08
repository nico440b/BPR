package com.example.bpr.Objects;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public
class Department {
    @SerializedName("Name")
    public String name;
    @SerializedName("Type")
    public String type;
    @SerializedName("OpeningHours")
    public List<OpeningHour> openingHours;
}
