package com.example.bpr.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.bpr.Converters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "coopstore")
@TypeConverters(Converters.class)
public class CoopStore implements Serializable {
    @PrimaryKey
    @SerializedName("Kardex")
    public int kardex;
    @SerializedName("RetailGroupName")
    public String retailGroupName;
    @SerializedName("Name")
    public String name;
    @SerializedName("Address")
    public String address;
    @SerializedName("Zipcode")
    public int zipcode;
    @SerializedName("City")
    public String city;
    @SerializedName("Phonenumber")
    public String phonenumber;
    @SerializedName("Manager")
    public String manager;
    @SerializedName("CVR")
    public int cVR;
    @SerializedName("FacebookLink")
    public String facebookLink;
    @SerializedName("BipAndPay")
    public boolean bipAndPay;
    @SerializedName("OrderOnline")
    public boolean orderOnline;
    @SerializedName("SelfCheckout")
    public boolean selfCheckout;
    @SerializedName("StoreId")
    public int storeId;
    @SerializedName("Links")
    public List<Link> links;
    @SerializedName("Location")
    public Location location;
    @SerializedName("OpeningHours")
    public List<OpeningHour> openingHours;
    @SerializedName("Departments")
    public List<Department> departments;


}

