package com.example.bpr.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.util.ArrayList;

@Entity(tableName = "coopstore")
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
    public ArrayList<Link> links;
    @SerializedName("Location")
    public Location location;
    @SerializedName("OpeningHours")
    public ArrayList<OpeningHour> openingHours;
    @SerializedName("Departments")
    public ArrayList<Department> departments;


}
@Entity
class Department{
    @SerializedName("Name")
    public String name;
    @SerializedName("Type")
    public String type;
    @SerializedName("OpeningHours")
    public ArrayList<OpeningHour> openingHours;
}
@Entity
class Link{
    @SerializedName("Type")
    public String type;
    @SerializedName("Text")
    public String text;
}
@Entity
class Location{
    public String type;
    public ArrayList<Double> coordinates;
}
@Entity
class OpeningHour{
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
