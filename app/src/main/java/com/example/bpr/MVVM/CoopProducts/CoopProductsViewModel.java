package com.example.bpr.MVVM.CoopProducts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bpr.Objects.CoopProducts;

import java.util.List;

public class CoopProductsViewModel extends AndroidViewModel {
    private CoopProductsRepository repository;
    private LiveData<List<CoopProducts>> products;
    public CoopProductsViewModel(@NonNull Application application) {
        super(application);
        repository = new CoopProductsRepository(application);
        products = repository.getAllProducts();
    }

    public void insert(CoopProducts coopProducts)
    {
        repository.insert(coopProducts);
    }
    public void insertAll(List<CoopProducts> coopProducts)
    {
        repository.insertAll(coopProducts);
    }

    public void delete()
    {
        repository.delete();
    }

    public LiveData<List<CoopProducts>> getProducts() {
        return products;
    }
}
