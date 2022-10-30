package com.example.bpr;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {CoopStoreCore.class,CoopProducts.class},version = 2,exportSchema = false
)

@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    //private static AppDatabase instance = null;
    public abstract CoopStoreDao coopStoreDao();
    public abstract CoopProductsDao coopProductsDao();

    /*
    public static AppDatabase getInstance() {
        if (instance == null)
        {
            instance = new AppDatabase();
        }
        return instance;
    }

     */
}
