package com.example.bpr;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CoopProductsDao {

    @Query("Select * From coopproducts")
    List<CoopProducts> getAll();

    @Insert
    void insertAll(List<CoopProducts> coopProducts);

    @Query("Delete From coopproducts")
    void deleteAll();

    @Query("Select * From coopproducts where navn LIKE :name")
    List<CoopProducts>findByName(String name);
}
