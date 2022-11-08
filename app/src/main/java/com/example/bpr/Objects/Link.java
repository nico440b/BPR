package com.example.bpr.Objects;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.bpr.Converters;
import com.google.gson.annotations.SerializedName;

@Entity
@TypeConverters(Converters.class)
public
class Link {
    @SerializedName("Type")
    public String type;
    @SerializedName("Text")
    public String text;
}
