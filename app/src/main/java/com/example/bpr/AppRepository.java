package com.example.bpr;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private CoopProductsDao coopProductsDao;
    private LiveData<List<CoopProducts>> allProducts;

    public AppRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        coopProductsDao = appDatabase.coopProductsDao();
        allProducts = coopProductsDao.getAll();
    }

    public void insert(List<CoopProducts> products) {
        new InsertCoopProductsAsyncTask(coopProductsDao).execute(products);
    }

    public void delete() {
        new DeleteCoopProductsAsyncTask(coopProductsDao).execute();
    }

    public LiveData<List<CoopProducts>> getAllProducts() {
        return allProducts;
    }

    private static class InsertCoopProductsAsyncTask extends AsyncTask<List<CoopProducts>, Void, Void> {
        private CoopProductsDao coopProductsDao;

        private InsertCoopProductsAsyncTask(CoopProductsDao coopProductsDao) {
            this.coopProductsDao = coopProductsDao;
        }

        @Override
        protected Void doInBackground(List<CoopProducts>... coopProducts) {
            coopProductsDao.insertAll(coopProducts[0]);
            return null;
        }
    }

    private static class DeleteCoopProductsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CoopProductsDao coopProductsDao;

        private DeleteCoopProductsAsyncTask(CoopProductsDao coopProductsDao) {
            this.coopProductsDao = coopProductsDao;
        }


        @Override
        protected Void doInBackground(Void... Void) {
            coopProductsDao.deleteAll();
            return null;
        }
    }
}

