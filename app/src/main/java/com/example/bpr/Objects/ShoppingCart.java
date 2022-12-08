package com.example.bpr.Objects;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {




    public LiveData<List<CoopProducts>> coopProducts;

    public ShoppingCart()
    {

    }

    public ShoppingCart(LiveData<List<CoopProducts>> coopProducts)
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
