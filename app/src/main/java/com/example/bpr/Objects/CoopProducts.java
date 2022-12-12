package com.example.bpr.Objects;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "coopproducts",primaryKeys = {"ean","store"})
public class CoopProducts {
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
        @NonNull
        public String store;
        public int kardex;
        public double longitude;
        public double latitude;
        public double amount=1;


        @NonNull
        public String getEan() {
                return ean;
        }

        public void setEan(@NonNull String ean) {
                this.ean = ean;
        }



        public String getNavn() {
                return navn;
        }

        public void setNavn(String navn) {
                this.navn = navn;
        }

        public String getNavn2() {
                return navn2;
        }

        public void setNavn2(String navn2) {
                this.navn2 = navn2;
        }

        public double getPris() {
                return pris;
        }

        public void setPris(double pris) {
                this.pris = pris;
        }

        public int getVareHierakiId() {
                return vareHierakiId;
        }

        public void setVareHierakiId(int vareHierakiId) {
                this.vareHierakiId = vareHierakiId;
        }

        public String getStore() {
                return store;
        }

        public void setStore(String store) {
                this.store = store;
        }

        public int getKardex() {
                return kardex;
        }

        public void setKardex(int kardex) {
                this.kardex = kardex;
        }

        public double getLongitude() {
                return longitude;
        }

        public void setLongitude(double longitude) {
                this.longitude = longitude;
        }

        public double getLatitude() {
                return latitude;
        }

        public void setLatitude(double latitude) {
                this.latitude = latitude;
        }

        public double getAmount() {
                return amount;
        }

        public void setAmount(double amount) {
                this.amount = amount;
        }

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
                location1.setLatitude(longitude);
                location1.setLongitude(latitude);
                Float distance = location.distanceTo(location1);
                return distance/1000 +" KM";
        }
        public double calculateDistanceDouble(Location location)
        {
                Location location1 = new Location("provider");
                location1.setLatitude(longitude);
                location1.setLongitude(latitude);
                Float distance = location.distanceTo(location1);
                return (double) distance/1000;
        }


        public double getShoppingCartProductPrice(){
                return amount*pris;
        }




}
