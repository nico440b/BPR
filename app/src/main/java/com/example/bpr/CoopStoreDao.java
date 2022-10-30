package com.example.bpr;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CoopStoreDao {

    @Query("Select * From coopstorecore")
    CoopStoreCore getAll();

    @Insert
    void insertAll(CoopStoreCore core);

    @Delete
    void delete(CoopStoreCore core);
}
