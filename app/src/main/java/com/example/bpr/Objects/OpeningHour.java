package com.example.bpr.Objects;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity
public
class OpeningHour {
    @SerializedName("Text")
    public String text;
    @SerializedName("Day")
    public String day;
    @SerializedName("From")
    public double from;
    @SerializedName("To")
    public double to;
    @SerializedName("FromDate")
    public String fromDate;
    @SerializedName("ToDate")
    public String toDate;
}
