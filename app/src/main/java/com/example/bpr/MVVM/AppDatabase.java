package com.example.bpr.MVVM;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bpr.Converters;
import com.example.bpr.Dao.CoopProductsDao;
import com.example.bpr.Dao.CoopStoreDao;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.CoopStoreCore;
import com.example.bpr.Objects.Location;
import com.example.bpr.VolleyCallBack;

import java.util.List;

@Database(entities = {CoopStore.class,CoopProducts.class},version = 8,exportSchema = false
)

@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;
    public abstract CoopStoreDao coopStoreDao();
    public abstract CoopProductsDao coopProductsDao();

    public static synchronized AppDatabase getInstance(Context context)

    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,"database").
                    fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private CoopProductsDao coopProductsDao;
        private NetworkImpl network = new NetworkImpl();
        private List<CoopProducts> list;

        private PopulateDbAsyncTask(AppDatabase appDatabase)
        {
            coopProductsDao = appDatabase.coopProductsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            network.getCoopProducts("test",0, null,new VolleyCallBack() {
                @Override
                public void onSuccessProducts(List<CoopProducts> result) {
                    list = result;
                }


            });
            coopProductsDao.insertAll(list);
            return null;
        }
    }
}
