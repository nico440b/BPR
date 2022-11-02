package com.example.bpr.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.CoopStoreCore;


@Dao
public interface CoopStoreDao {

    @Query("Select * From coopstorecore")
    CoopStoreCore getAll();

    @Insert
    void insertAll(CoopStoreCore core);

    @Delete
    void delete(CoopStoreCore core);
}
