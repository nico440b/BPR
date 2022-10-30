package com.example.bpr;

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




}
