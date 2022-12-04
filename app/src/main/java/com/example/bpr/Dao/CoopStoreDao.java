package com.example.bpr.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.CoopStoreCore;

import java.util.List;


@Dao
public interface CoopStoreDao {

    @Query("Select * From coopstore")
    LiveData<List<CoopStore>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<CoopStore> coopStores);

    @Query("Delete From coopstore")
    void deleteAll();
}
