package com.example.bpr;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {CoopStoreCore.class,CoopProducts.class},version = 2,exportSchema = false
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
            network.CoopProductsAPI(new VolleyCallBack() {
                @Override
                public void onSuccess(List<CoopProducts> result) {
                    list = result;
                }
            });
            coopProductsDao.insertAll(list);
            return null;
        }
    }
}
