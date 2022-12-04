package com.example.bpr.MVVM.CoopStores;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bpr.Objects.CoopStore;

import java.util.List;

public class CoopStoresViewModel extends AndroidViewModel {
    private CoopStoresRepository coopStoresRepository;
    private LiveData<List<CoopStore>> stores;

    public CoopStoresViewModel(@NonNull Application application) {
        super(application);
        coopStoresRepository = new CoopStoresRepository(application);
        stores = coopStoresRepository.getAll();
    }


    public void insertAll(List<CoopStore> coopStores)
    {
        coopStoresRepository.insertAll(coopStores);
    }

    public void deleteAll()
    {
        coopStoresRepository.deleteAll();
    }
    public LiveData<List<CoopStore>> getAll()
    {
        return coopStoresRepository.getAll();
    }
}
