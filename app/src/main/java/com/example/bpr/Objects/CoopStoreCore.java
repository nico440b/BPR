package com.example.bpr.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class CoopStoreCore {
    @PrimaryKey
    @SerializedName("CurrentPage")
    public int currentPage;
    @SerializedName("PageCount")
    public int pageCount;
    @SerializedName("PageSize")
    public int pageSize;
    @SerializedName("TotalPagedItemsCount")
    public int totalPagedItemsCount;
    /*
    @SerializedName("Data")
    public ArrayList<CoopStore> data;

     */
    @SerializedName("ApiObsolete")
    public boolean apiObsolete;
    @SerializedName("ApiVersion")
    public String apiVersion;
    @SerializedName("Status")
    public int status;
    @SerializedName("Message")
    public String message;


}
