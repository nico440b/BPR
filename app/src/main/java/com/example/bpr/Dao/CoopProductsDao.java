package com.example.bpr.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bpr.Objects.CoopProducts;

import java.util.List;

@Dao
public interface CoopProductsDao {

    @Query("Select * From coopproducts")
    LiveData<List<CoopProducts>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<CoopProducts> coopProducts);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOne(CoopProducts coopProducts);

    @Query("Delete From coopproducts")
    void deleteAll();

    @Query("Select * From coopproducts where navn LIKE :name")
    List<CoopProducts>findByName(String name);
}
