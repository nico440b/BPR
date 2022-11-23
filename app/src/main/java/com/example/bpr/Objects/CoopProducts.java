package com.example.bpr.Objects;

import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "coopproducts")
public class CoopProducts {
        @PrimaryKey
        @NonNull
        @SerializedName("Ean")
        public String ean;
        @SerializedName("Navn")
        public String navn;
        @SerializedName("Navn2")
        public String navn2;
        @SerializedName("Pris")
        public double pris;
        @SerializedName("VareHierakiId")
        public int vareHierakiId;
        public String store;
        public int kardex;
        public double longitude;
        public double latitude;


        public Location getLocation()
        {
                Location location = new Location("provider");
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                return new Location(location);
        }

        public String calculateDistanceString(Location location)
        {
                Location location1 = new Location("provider");
                location1.setLatitude(latitude);
                location1.setLongitude(longitude);
                Float distance = location.distanceTo(location1);
                return distance/1000000 +" KM";
        }
        public double calculateDistanceDouble(Location location)
        {
                Location location1 = new Location("provider");
                location1.setLatitude(latitude);
                location1.setLongitude(longitude);
                Float distance = location.distanceTo(location1);
                return (double) distance/1000000;
        }




}
