package com.example.bpr.MVVM.CoopStores;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bpr.Dao.CoopProductsDao;
import com.example.bpr.Dao.CoopStoreDao;
import com.example.bpr.MVVM.AppDatabase;
import com.example.bpr.MVVM.CoopProducts.CoopProductsRepository;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.CoopStoreCore;

import java.util.List;

public class CoopStoresRepository {
    private CoopStoreDao coopStoreDao;
    private LiveData<List<CoopStore>> stores;

    public CoopStoresRepository(Application application)
    {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        coopStoreDao = appDatabase.coopStoreDao();
        stores = coopStoreDao.getAll();
    }

    public void insertAll(List<CoopStore> stores)
    {
        new CoopStoresRepository.InsertAllCoopStoresAsyncTask(coopStoreDao).execute(stores);
    }
    public LiveData<List<CoopStore>> getAll() {
        return stores;
    }

    public void deleteAll() {
        new CoopStoresRepository.DeleteCoopProductsAsyncTask(coopStoreDao).execute();
    }

    private static class InsertAllCoopStoresAsyncTask extends AsyncTask<List<CoopStore>, Void, Void> {
        private CoopStoreDao coopStoreDao;

        private InsertAllCoopStoresAsyncTask(CoopStoreDao coopStoreDao) {
            this.coopStoreDao = coopStoreDao;
        }

        @Override
        protected Void doInBackground(List<CoopStore>... stores) {
            coopStoreDao.insertAll(stores[0]);
            return null;
        }
    }

    private static class DeleteCoopProductsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CoopStoreDao coopStoreDao;

        private DeleteCoopProductsAsyncTask(CoopStoreDao coopStoreDao) {
            this.coopStoreDao = coopStoreDao;
        }


        @Override
        protected Void doInBackground(Void... Void) {
            coopStoreDao.deleteAll();
            return null;
        }
    }
}
