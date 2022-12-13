package com.example.bpr.Objects;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteList {
    public LiveData<List<CoopProducts>> coopProducts;

    public FavoriteList()
    {

    }

    public FavoriteList(LiveData<List<CoopProducts>> coopProducts)
    {
        this.coopProducts = coopProducts;
    }

    public LiveData<List<CoopProducts>> getCoopProducts() {
        return coopProducts;
    }

    public void setCoopProducts(LiveData<List<CoopProducts>> coopProducts) {
        this.coopProducts = coopProducts;
    }
}
